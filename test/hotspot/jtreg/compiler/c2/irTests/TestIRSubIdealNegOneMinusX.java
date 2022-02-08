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
 * @summary Test that transformation from -1-x to ~x works as intended.
 * @library /test/lib /
 * @run driver compiler.c2.irTests.TestIRNegOneMinusX
 */
public class TestIRSubIdealNegOneMinusX {

    public static void main(String[] args) {
        TestFramework.run();
    }

    @Test
    @IR(failOn = {IRNode.SUB_I, IRNode.ADD_I})
    @IR(counts = {IRNode.XOR_I, "1"})
    public int testInt(int x) {
        return -1 - x; // transformed to x ^ (-1)
    }

    @Run(test = "testInt")
    public void checkTestInt(RunInfo info) {
        assertC2Compiled(info);
        Asserts.assertEquals(-11, testInt(10));
        Asserts.assertEquals(-1, testInt(0));
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
