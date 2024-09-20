
#include "common/errors.h"
#include "common/types.h"

static SimpleError the_simple_error;

Error FormatError(const char *fmt, ...)
{
    the_simple_error.Message = fmt;
    return &the_simple_error;
}
