package com.utopia.data.transfer.model.code.config;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.utopia.data.transfer.model.code.entity.EventColumn;
import com.utopia.data.transfer.model.code.entity.data.EventData;
import com.utopia.data.transfer.model.code.entity.data.EventDataTransaction;
import com.utopia.data.transfer.model.code.entity.data.Message;
import com.utopia.data.transfer.model.code.event.EventType;
import com.utopia.data.transfer.model.code.transfer.TransferUniqueDesc;
import com.utopia.data.transfer.model.code.transfer.TransferUniquePair;
import com.utopia.serialization.kryo.utils.KryoIdUnique;
import com.utopia.serialization.kryo.utils.KryoUtils;
import com.utopia.serialization.kryo.utils.SupplierSerializer;

/**
 * @author owen.cai
 * @create_date 2021/11/10
 * @alter_author
 * @alter_date
 */
public enum KryoRegister {

    MessageId(Message.class, 20001, (kryo)-> new CompatibleFieldSerializer(kryo, Message.class)),
    EventDataTransactionId(EventDataTransaction.class, 20002, (kryo)-> new CompatibleFieldSerializer(kryo, EventDataTransaction.class)),
    TransferUniqueDescId(TransferUniqueDesc.class, 20003, (kryo)-> new CompatibleFieldSerializer(kryo, TransferUniqueDesc.class)),
    EventDataId(EventData.class, 20004, (kryo)-> new CompatibleFieldSerializer(kryo, EventData.class)),
    EventColumnId(EventColumn.class, 20005, (kryo)-> new CompatibleFieldSerializer(kryo, EventColumn.class)),
    TransferUniquePairId(TransferUniquePair.class, 20006, (kryo)-> new CompatibleFieldSerializer(kryo, TransferUniquePair.class)),
    EventTypeId(EventType.class, 20007, (kryo)->{
        return new Serializer<EventType>() {
            @Override
            public void write(Kryo kryo, Output output, EventType object) {
                output.writeString(object.getValue());
            }

            @Override
            public EventType read(Kryo kryo, Input input, Class<EventType> type) {
                return EventType.valuesOf(input.readString());
            }
        };
    })
    ;

    private final KryoIdUnique kryoIdUnique;
    KryoRegister(Class messageClass, int i, SupplierSerializer supplierSerializer) {
        kryoIdUnique = KryoIdUnique.builder()
                .classType(messageClass)
                .id(i)
                .supplier(supplierSerializer)
                .build();
    }

    public KryoIdUnique getKryoIdUnique() {
        return kryoIdUnique;
    }

    public static void registerKryo() {
        KryoUtils.setRegistrationRequired(true);
        for (KryoRegister value : KryoRegister.values()) {
            //注册
            KryoUtils.register(value.getKryoIdUnique());
        }
    }
}
