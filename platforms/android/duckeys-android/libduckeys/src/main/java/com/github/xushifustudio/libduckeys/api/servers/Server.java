package com.github.xushifustudio.libduckeys.api.servers;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;

import java.net.URI;

public interface Server {

    Have invoke(Want want) throws Exception;

    API findAPI(Want want);

    API getMidiWriterAPI();

    API getMidiReaderAPI();

}
