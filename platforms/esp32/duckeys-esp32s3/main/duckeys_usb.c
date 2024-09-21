

#include "duckeys_libs.h"
#include "duckeys_usb.h"
#include "duckeys_app.h"

// Basic MIDI Messages
#define NOTE_OFF 0x80
#define NOTE_ON 0x90

// Interface counter
enum interface_count
{
#if CFG_TUD_MIDI
    ITF_NUM_MIDI = 0,
    ITF_NUM_MIDI_STREAMING,
#endif
    ITF_COUNT
};

// USB Endpoint numbers
enum usb_endpoints
{
    // Available USB Endpoints: 5 IN/OUT EPs and 1 IN EP
    EP_EMPTY = 0,
#if CFG_TUD_MIDI
    EPNUM_MIDI,
#endif
};

#define TUSB_DESCRIPTOR_TOTAL_LEN (TUD_CONFIG_DESC_LEN + CFG_TUD_MIDI * TUD_MIDI_DESC_LEN)

static const char *s_str_desc[5] = {
    // array of pointer to string descriptors
    (char[]){0x09, 0x04}, // 0: is supported language is English (0x0409)
    "XuShiFu",            // 1: Manufacturer
    "Duckeys",            // 2: Product
    "100001",             // 3: Serials, should use chip ID
    "Duckeys",            // 4: MIDI
};

static DuckeysUSB *the_duckeys_usb_inst = Nil;

/* A combination of interfaces must have a unique product id, since PC will save device driver after the first plug.
 * Same VID/PID with different interface e.g MSC (first), then CDC (later) will possibly cause system error on PC.
 *
 * Auto ProductID layout's Bitmap:
 *   [MSB]       MIDI | HID | MSC | CDC          [LSB]
 */
#define _PID_MAP(itf, n) ((CFG_TUD_##itf) << (n))
#define USB_PID (0x4000 | _PID_MAP(CDC, 0) | _PID_MAP(MSC, 1) | _PID_MAP(HID, 2) | \
                 _PID_MAP(MIDI, 3) | _PID_MAP(VENDOR, 4))

//--------------------------------------------------------------------+
// Device Descriptors
//--------------------------------------------------------------------+
tusb_desc_device_t const desc_device =
    {
        .bLength = sizeof(tusb_desc_device_t),
        .bDescriptorType = TUSB_DESC_DEVICE,
        .bcdUSB = 0x0200,
        .bDeviceClass = 0x00,
        .bDeviceSubClass = 0x00,
        .bDeviceProtocol = 0x00,
        .bMaxPacketSize0 = CFG_TUD_ENDPOINT0_SIZE,

        .idVendor = 0xCafe,
        .idProduct = USB_PID,
        .bcdDevice = 0x0100,

        .iManufacturer = 0x01,
        .iProduct = 0x02,
        .iSerialNumber = 0x03,

        .bNumConfigurations = 0x01};

static const uint8_t s_midi_cfg_desc[] = {
    // Configuration number, interface count, string index, total length, attribute, power in mA
    TUD_CONFIG_DESCRIPTOR(1, ITF_COUNT, 0, TUSB_DESCRIPTOR_TOTAL_LEN, 0, 100),

    // Interface number, string index, EP Out & EP In address, EP size
    TUD_MIDI_DESCRIPTOR(ITF_NUM_MIDI, 4, EPNUM_MIDI, (0x80 | EPNUM_MIDI), 64),
};

// 处理转发过来的上行数据
int duckeys_hub_handle_up_bytes(DuckeysHub *self, const DK_BYTE *data, DK_LENGTH len)
{
    static uint8_t const cable_num = 0; // MIDI jack associated with USB endpoint
    if (tud_midi_mounted() && (len > 0))
    {
        tud_midi_stream_write(cable_num, data, len);
        return len;
    }
    return 0;
}

void duckeys_usb_debug_ontimer()
{
    DuckeysUSB *self = the_duckeys_usb_inst;
    if (self == Nil)
    {
        return;
    }

    long count = self->debug_count;
    DK_BYTE buffer[3];
    if (count & 0x01)
    {
        // note no
        buffer[0] = NOTE_ON;
        buffer[1] = self->debug_note;
        buffer[2] = 90;
    }
    else
    {
        // note off
        buffer[0] = NOTE_OFF;
        buffer[1] = self->debug_note;
        buffer[2] = 0;
        int note = self->debug_note + 1;
        self->debug_note = (note & 0x1f) + (32);
    }
    self->debug_count++;

    duckeys_hub_handle_up_bytes(self->hub, buffer, sizeof(buffer));
}

Error duckeys_usb_init(DuckeysUSB *self, DuckeysApp *app)
{
    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_usb_init - begin");

    the_duckeys_usb_inst = self;

    tinyusb_config_t const tusb_cfg = {
        .device_descriptor = &desc_device, // If device_descriptor is NULL, tinyusb_driver_install() will use Kconfig
        .string_descriptor = s_str_desc,
        .string_descriptor_count = sizeof(s_str_desc) / sizeof(s_str_desc[0]),
        .external_phy = false,
#if (TUD_OPT_HIGH_SPEED)
        .fs_configuration_descriptor = s_midi_cfg_desc, // HID configuration descriptor for full-speed and high-speed are the same
        .hs_configuration_descriptor = s_midi_hs_cfg_desc,
        .qualifier_descriptor = NULL,
#else
        .configuration_descriptor = s_midi_cfg_desc,
#endif // TUD_OPT_HIGH_SPEED
    };
    ESP_ERROR_CHECK(tinyusb_driver_install(&tusb_cfg));

    ESP_LOGI(DUCKEYS_LOG_TAG, "duckeys_usb_init - end");
    return Nil;
}
