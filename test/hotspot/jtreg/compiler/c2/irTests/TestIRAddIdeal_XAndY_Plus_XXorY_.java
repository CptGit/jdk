/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package compiler.c2.irTests;

import jdk.test.lib.Asserts;
import compiler.lib.ir_framework.*;

/*
 * @test
 * @summary Test that transformation from "(x&y)+(x^y)" to "x|y" works
 *          as intended.
 * @library /test/lib /
 * @run driver compiler.c2.irTests.TestIRAddIdeal_XAndY_Plus_XXorY_
 */
public class TestIRAddIdeal_XAndY_Plus_XXorY_ {

    public static void main(String[] args) {
        TestFramework.run();
    }

    @Test
    @IR(failOn = {IRNode.AND_I, IRNode.XOR_I, IRNode.ADD_I})
    @IR(counts = {IRNode.OR_I, "1"})
    public int testInt1(int x, int y) {
        return (x & y) + (x ^ y); // transformed to x | y
    }

    @Run(test = "testInt1")
    public void checkTestInt1(RunInfo info) {
        assertC2Compiled(info);
        Asserts.assertEquals(0x5F5F_AFAF, testInt1(0x5A5A_A5A5, 0x5555_AAAA));
        Asserts.assertEquals(0x0000_0000, testInt1(0x0000_0000, 0x0000_0000));
        Asserts.assertEquals(0xFFFF_FFFF, testInt1(0xFFFF_FFFF, 0xFFFF_FFFF));
        Asserts.assertEquals(0xFFFF_FFFF, testInt1(0xFFFF_FFFF, 0x0000_0000));
        Asserts.assertEquals(0xFFFF_FFFF, testInt1(0x0000_0000, 0xFFFF_FFFF));
        Asserts.assertEquals(0x8000_0000, testInt1(0x8000_0000, 0x8000_0000));
        Asserts.assertEquals(0x7FFF_FFFF, testInt1(0x7000_0000, 0x7FFF_FFFF));
    }

    @Test
    @IR(failOn = {IRNode.AND_I, IRNode.XOR_I, IRNode.ADD_I})
    @IR(counts = {IRNode.OR_I, "1"})
    public int testInt2(int x, int y) {
        return (x ^ y) + (x & y); // transformed to x | y
    }

    @Run(test = "testInt2")
    public void checkTestInt2(RunInfo info) {
        assertC2Compiled(info);
        Asserts.assertEquals(0x5F5F_AFAF, testInt2(0x5A5A_A5A5, 0x5555_AAAA));
        Asserts.assertEquals(0x0000_0000, testInt2(0x0000_0000, 0x0000_0000));
        Asserts.assertEquals(0xFFFF_FFFF, testInt2(0xFFFF_FFFF, 0xFFFF_FFFF));
        Asserts.assertEquals(0xFFFF_FFFF, testInt2(0xFFFF_FFFF, 0x0000_0000));
        Asserts.assertEquals(0xFFFF_FFFF, testInt2(0x0000_0000, 0xFFFF_FFFF));
        Asserts.assertEquals(0x8000_0000, testInt2(0x8000_0000, 0x8000_0000));
        Asserts.assertEquals(0x7FFF_FFFF, testInt2(0x7000_0000, 0x7FFF_FFFF));
    }

    @Test
    @IR(failOn = {IRNode.AND_L, IRNode.XOR_L, IRNode.ADD_L})
    @IR(counts = {IRNode.OR_L, "1"})
    public long testLong1(long x, long y) {
        return (x & y) + (x ^ y); // transformed to x | y
    }

    @Run(test = "testLong1")
    public void checkTestLong1(RunInfo info) {
        assertC2Compiled(info);
        Asserts.assertEquals(0x5F5F_AFAF_FFFF_5FAFL, testLong1(0x5A5A_A5A5_5AA5_55AAL, 0x5555_AAAA_A55A_5AA5L));
        Asserts.assertEquals(0x0000_0000_0000_0000L, testLong1(0x0000_0000_0000_0000L, 0x0000_0000_0000_0000L));
        Asserts.assertEquals(0xFFFF_FFFF_FFFF_FFFFL, testLong1(0xFFFF_FFFF_FFFF_FFFFL, 0xFFFF_FFFF_FFFF_FFFFL));
        Asserts.assertEquals(0xFFFF_FFFF_FFFF_FFFFL, testLong1(0xFFFF_FFFF_FFFF_FFFFL, 0x0000_0000_0000_0000L));
        Asserts.assertEquals(0xFFFF_FFFF_FFFF_FFFFL, testLong1(0x0000_0000_0000_0000L, 0xFFFF_FFFF_FFFF_FFFFL));
        Asserts.assertEquals(0x8000_0000_0000_0000L, testLong1(0x8000_0000_0000_0000L, 0x8000_0000_0000_0000L));
        Asserts.assertEquals(0x7FFF_FFFF_FFFF_FFFFL, testLong1(0x7FFF_0000_0000_0000L, 0x7FFF_FFFF_FFFF_FFFFL));
    }

    @Test
    @IR(failOn = {IRNode.AND_L, IRNode.XOR_L, IRNode.ADD_L})
    @IR(counts = {IRNode.OR_L, "1"})
    public long testLong2(long x, long y) {
        return (x ^ y) + (x & y); // transformed to x | y
    }

    @Run(test = "testLong2")
    public void checkTestLong2(RunInfo info) {
        assertC2Compiled(info);
        Asserts.assertEquals(0x5F5F_AFAF_FFFF_5FAFL, testLong2(0x5A5A_A5A5_5AA5_55AAL, 0x5555_AAAA_A55A_5AA5L));
        Asserts.assertEquals(0x0000_0000_0000_0000L, testLong2(0x0000_0000_0000_0000L, 0x0000_0000_0000_0000L));
        Asserts.assertEquals(0xFFFF_FFFF_FFFF_FFFFL, testLong2(0xFFFF_FFFF_FFFF_FFFFL, 0xFFFF_FFFF_FFFF_FFFFL));
        Asserts.assertEquals(0xFFFF_FFFF_FFFF_FFFFL, testLong2(0xFFFF_FFFF_FFFF_FFFFL, 0x0000_0000_0000_0000L));
        Asserts.assertEquals(0xFFFF_FFFF_FFFF_FFFFL, testLong2(0x0000_0000_0000_0000L, 0xFFFF_FFFF_FFFF_FFFFL));
        Asserts.assertEquals(0x8000_0000_0000_0000L, testLong2(0x8000_0000_0000_0000L, 0x8000_0000_0000_0000L));
        Asserts.assertEquals(0x7FFF_FFFF_FFFF_FFFFL, testLong2(0x7FFF_0000_0000_0000L, 0x7FFF_FFFF_FFFF_FFFFL));
    }

    private void assertC2Compiled(RunInfo info) {
        // Test VM allows C2 to work
        Asserts.assertTrue(info.isC2CompilationEnabled());
        if (!info.isWarmUp()) {
            // C2 compilation happens
            Asserts.assertTrue(info.isTestC2Compiled());
        }
    }
}
