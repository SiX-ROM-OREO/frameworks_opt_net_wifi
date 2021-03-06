/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.wifi.hotspot2.anqp.eap;

import static org.junit.Assert.assertEquals;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import java.net.ProtocolException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Unit tests for {@link com.android.server.wifi.hotspot2.anqp.eap.CredentialType}.
 */
@SmallTest
public class CredentialTypeTest {
    private static final int TEST_TYPE = CredentialType.CREDENTIAL_TYPE_USIM;

    /**
     * Helper function for generating the test buffer.
     *
     * @return {@link ByteBuffer}
     */
    private ByteBuffer getTestBuffer() {
        return ByteBuffer.wrap(new byte[] {(byte) TEST_TYPE});
    }

    /**
     * Verify that BufferUnderflowException will be thrown when parsing from an empty buffer.
     *
     * @throws Exception
     */
    @Test(expected = BufferUnderflowException.class)
    public void parseEmptyBuffer() throws Exception {
        CredentialType.parse(
                ByteBuffer.wrap(new byte[0]), CredentialType.EXPECTED_LENGTH_VALUE, false);
    }

    /**
     * Verify that ProtocolException will be thrown when the data length value is not the same
     * as the expected
     *
     * @throws Exception
     */
    @Test(expected = ProtocolException.class)
    public void parseBufferWithInvalidLength() throws Exception {
        CredentialType.parse(getTestBuffer(), CredentialType.EXPECTED_LENGTH_VALUE - 1, false);
    }

    /**
     * Verify that an expected CredentialType is returned when parsing the buffer for a
     * non-tunneled EAP method.
     *
     * @throws Exception
     */
    @Test
    public void parseBufferForNonTunneledEAPMethod() throws Exception {
        CredentialType expected =
                new CredentialType(AuthParam.PARAM_TYPE_CREDENTIAL_TYPE, TEST_TYPE);
        CredentialType actual = CredentialType.parse(
                getTestBuffer(), CredentialType.EXPECTED_LENGTH_VALUE, false);
        assertEquals(expected, actual);
    }

    /**
     * Verify that an expected CredentialType is returned when parsing the buffer for a
     * tunneled EAP method.
     *
     * @throws Exception
     */
    @Test
    public void parseBufferForTunneledEAPMethod() throws Exception {
        CredentialType expected = new CredentialType(
                AuthParam.PARAM_TYPE_TUNNELED_EAP_METHOD_CREDENTIAL_TYPE, TEST_TYPE);
        CredentialType actual = CredentialType.parse(
                getTestBuffer(), CredentialType.EXPECTED_LENGTH_VALUE, true);
        assertEquals(expected, actual);
    }
}
