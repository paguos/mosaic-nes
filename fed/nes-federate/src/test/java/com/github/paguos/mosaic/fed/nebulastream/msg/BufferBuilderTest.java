package com.github.paguos.mosaic.fed.nebulastream.msg;

import com.github.paguos.mosaic.fed.nebulastream.stream.BufferBuilder;
import org.junit.Test;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BufferBuilderTest {

    @Test
    public void fillByte() {
        byte aByte = 120;
        byte[] buffer = BufferBuilder.createBuffer(1).fill(aByte).build();
        assertArrayEquals(new byte[]{120}, buffer);
    }

    @Test
    public void fillBytes() {
        byte[] bytes = new byte[]{5, 24, 32, 8, 9, 11};
        byte[] buffer = BufferBuilder.createBuffer(6).fill(bytes).build();
        assertArrayEquals(bytes, buffer);
    }

    @Test
    public void fillShort() {
        short aShort = 130;
        byte[] buffer = BufferBuilder.createBuffer(2).fill(aShort).build();
        byte[] expectedBuffer = {-126, 0};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void fillInt() {
        byte[] buffer = BufferBuilder.createBuffer(4).fill(33000).build();
        byte[] expectedBuffer = {-24, -128, 0, 0};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void fillLong() {
        byte[] buffer = BufferBuilder.createBuffer(8).fill(2300000000L).build();
        byte[] expectedBuffer = {0, 55, 23, -119, 0, 0, 0, 0};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void fillFloat() {
        byte[] buffer = BufferBuilder.createBuffer(4).fill(32.12345F).build();
        byte[] expectedBuffer = {106, 126, 0, 66};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void fillDouble() {
        byte[] buffer = BufferBuilder.createBuffer(8).fill(10.12345678D).build();
        byte[] expectedBuffer = {-111, 35, 33, -70, 53, 63, 36, 64};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void fillString() {
        byte[] buffer = BufferBuilder.createBuffer(7).fill("veh_100", 7).build();
        assertEquals("veh_100", new String(buffer));
    }

    @Test
    public void fillShorterString() {
        byte[] buffer = BufferBuilder.createBuffer(7).fill("veh_1", 7).build();
        byte[] expectedBuffer = {118, 101, 104, 95, 49, 0, 0};
        assertArrayEquals(expectedBuffer, buffer);
        assertEquals("veh_1", new String(buffer).trim());
    }

    @Test
    public void parseSpeedReportTuple() {
        byte[] buffer = BufferBuilder.createBuffer(27)
                .fill("veh_100", 7)
                .fill(10000)
                .fill(1300000000L)
                .fill(13.322302D)
                .build();

        byte[] expectedBuffer = {118, 101, 104, 95, 49, 48, 48, 16, 39, 0, 0, 0, 109, 124, 77, 0, 0, 0, 0, -20, -34, -118, -60, 4, -91, 42, 64};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void parseSpeedReportMosaicTuple() {
        byte[] buffer = BufferBuilder.createBuffer(39)
                .fill("veh_71", 7)
                .fill(212000000000L)
                .fill(52.512124D)
                .fill(13.321144D)
                .fill(9.355131D)
                .build();
        byte[] expectedBuffer = {118, 101, 104, 95, 55, 49, 0, 0, 72, 47, 92, 49, 0, 0, 0, -108, -65, 123, 71, -115, 65, 74, 64, -2, -100, -126, -4, 108, -92, 42, 64, 112, -105, -3, -70, -45, -75, 34, 64};
        assertArrayEquals(expectedBuffer, buffer);
    }

}
