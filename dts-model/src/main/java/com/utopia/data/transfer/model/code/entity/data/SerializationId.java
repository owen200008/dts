package com.utopia.data.transfer.model.code.entity.data;

import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.exception.UtopiaException;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.serialization.api.SerializationApi;
import com.utopia.string.UtopiaBinaryReader;
import com.utopia.string.UtopiaBinaryWriter;
import lombok.Getter;

/**
 * @author owen.cai
 * @create_date 2021/6/16
 * @alter_author
 * @alter_date
 */
public enum SerializationId {
    kryo((byte)1);

    @Getter
    byte value;
    SerializationId(byte b) {
        this.value = b;
    }

    private static byte SIGN_BEGIN = 0x21;
    private static byte SIGN_END = 0x76;
    private static byte DATA_VERSION = 1;
    public static <T> byte[] serialization(SerializationId serializationId, SerializationApi serializationApi, T value){
        UtopiaBinaryWriter utopiaBinaryWriter = new UtopiaBinaryWriter(1024);
        utopiaBinaryWriter.append(SIGN_BEGIN);
        utopiaBinaryWriter.append(DATA_VERSION);
        utopiaBinaryWriter.append(SIGN_END);
        utopiaBinaryWriter.append(serializationId.getValue());
        Object context = serializationApi.initWrite(utopiaBinaryWriter);
        serializationApi.writeNoClass(value, context);
        serializationApi.finishWrite(context);
        return utopiaBinaryWriter.build();
    }

    public static <T> T derialization(Class<T> t, byte[] data) throws UtopiaException {
        UtopiaBinaryReader utopiaBinaryReader = new UtopiaBinaryReader();
        utopiaBinaryReader.initWithRefData(data);
        byte signBegin = utopiaBinaryReader.readByte();
        if(signBegin != SIGN_BEGIN){
            //格式错误
            throw new UtopiaException(ErrorCode.DERIALIZATION_FORMAT_ERROR);
        }
        byte dataVersion = utopiaBinaryReader.readByte();
        byte signEnd = utopiaBinaryReader.readByte();
        if(signEnd != SIGN_END){
            //格式错误
            throw new UtopiaException(ErrorCode.DERIALIZATION_FORMAT_ERROR);
        }
        if(dataVersion != DATA_VERSION){
            throw new UtopiaException(ErrorCode.DERIALIZATION_VERSION_ERROR);
        }
        byte serializationId = utopiaBinaryReader.readByte();
        if(serializationId == kryo.value){
            SerializationApi serializationApi = UtopiaExtensionLoader.getExtensionLoader(SerializationApi.class).getExtension(kryo.name());
            if(serializationApi == null){
                throw new UtopiaException(ErrorCode.DERIALIZATION_NOFIND_SERIALIZE);
            }
            Object context = serializationApi.initRead(utopiaBinaryReader);
            return serializationApi.readNoClass(t,context);
        }
        else{
            throw new UtopiaException(ErrorCode.DERIALIZATION_UNSUPPORT_SERIALIZE);
        }
    }
}
