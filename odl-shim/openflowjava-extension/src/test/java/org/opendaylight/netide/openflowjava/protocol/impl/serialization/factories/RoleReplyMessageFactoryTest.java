/*
 * Copyright (c) 2015 NetIDE Consortium and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netide.openflowjava.protocol.impl.serialization.factories;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opendaylight.netide.openflowjava.protocol.impl.serialization.NetIdeSerializerRegistryImpl;
import org.opendaylight.netide.openflowjava.protocol.impl.util.BufferHelper;
import org.opendaylight.openflowjava.protocol.api.extensibility.SerializerRegistry;
import org.opendaylight.openflowjava.protocol.api.util.EncodeConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.ControllerRole;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.RoleRequestOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.RoleRequestOutputBuilder;

/**
 * @author giuseppex.petralia@intel.com
 *
 */
public class RoleReplyMessageFactoryTest {
    RoleRequestOutput message;
    private static final byte MESSAGE_TYPE = 25;
    private static final byte PADDING = 4;

    @Before
    public void startUp() throws Exception {
        RoleRequestOutputBuilder builder = new RoleRequestOutputBuilder();
        BufferHelper.setupHeader(builder, EncodeConstants.OF13_VERSION_ID);
        builder.setRole(ControllerRole.forValue(0));
        builder.setGenerationId(BigInteger.valueOf(1L));
        message = builder.build();
    }

    @Test
    public void testSerialize() {
        RoleReplyMessageFactory serializer = new RoleReplyMessageFactory();
        SerializerRegistry registry = new NetIdeSerializerRegistryImpl();
        registry.init();
        ByteBuf serializedBuffer = UnpooledByteBufAllocator.DEFAULT.buffer();
        serializer.serialize(message, serializedBuffer);
        BufferHelper.checkHeaderV13(serializedBuffer, MESSAGE_TYPE, 24);
        Assert.assertEquals("Wrong role", message.getRole().getIntValue(),
                ControllerRole.forValue((int) serializedBuffer.readUnsignedInt()).getIntValue());
        serializedBuffer.skipBytes(PADDING);
        byte[] genId = new byte[EncodeConstants.SIZE_OF_LONG_IN_BYTES];
        serializedBuffer.readBytes(genId);
        Assert.assertEquals("Wrong generation ID", message.getGenerationId(), new BigInteger(1, genId));
    }
}
