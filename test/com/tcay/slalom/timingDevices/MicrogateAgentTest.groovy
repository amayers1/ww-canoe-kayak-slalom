/*
 * This file is part of SlalomApp.
 *
 *     SlalomApp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SlalomApp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SlalomApp.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tcay.slalom.timingDevices

import com.tcay.slalom.timingDevices.MicrogateREI2.MicrogateAgent
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

/**
 * Created by allen on 3/20/16.
 */
class MicrogateAgentTest {
    MicrogateAgent microgateAgent;

    @BeforeClass
    public void setUp() {
        microgateAgent = new MicrogateAgent();
    }

    @AfterClass
    public void tearDown() {
        microgateAgent = null;
    }

/*
    @Test(groups = ["unit"])
    void testStartRacer() {



    }


    @Test(groups = ["unit"])
    void testStopRacer() {


    }


*/


@Test(groups = ["unit"])
    void testStream() {
        String[] microgateEmulatedOutput = [

            // R - Always
            // |  S - Single Starts - All we use
            // |  |O - "Online"
            // |  ||xxxxxx - progressive counter
            // |  ||     |ccccc - Competitor
            // |  ||     |    |ggg - group/category
            // |  ||     |    |  |rrr - run #
            // |  ||     |    |  |  |ppp - physical channel #
            // |  ||     |    |  |  |  |lll - logical channel #
            // |  ||     |    |  |  |  |  |
            // |  ||     |    |  |  |  |  |i - information
            // |  ||     |    |  |  |  |  ||tttttttttt - time/speed
            // |  ||     |    |  |  |  |  ||         |
            // |  ||     |    |  |  |  |  ||         |ddmmyyyy - date
            // |  ||     |    |  |  |  |  ||         |       |
            // v  vv     v    v  v  v  v  vv         v       v
            "  R  SO000156000010000011000000000339347001012009\r\n",
            "  R  SO000157000020000021152550000346767001012009\r\n",
            "  R  SO000158000020000011000000000353038101012009\r\n",
            "  R  SO000159000030000010000000001052915101012009\r\n",
            "  R  SO000160000040000010000000001100012701012009\r\n",
            "  R  SO000161000020000010152550001121409301012009\r\n",
            "  R  SO000162000050000010000000001133452101012009\r\n",
            "  R  SO000163000060000010000000001228208101012009\r\n",
            "  R  SO000164000030000010152550001123857301012009\r\n",
            "  R  SO000165000040000010152550001131641201012009\r\n",
            "  R  SO000166000050000010152550001136251701012009\r\n",
            "  R  SO000167000060000010152550001144289701012009\r\n",
            "  R  SO000168000010000010152550001220282101012009\r\n",
            "  R  SO000169000000000010152550001222625401012009\r\n",
            "  R  SO0001700000200000101525520007283700+0000000\r\n",
            "  R  SO0001710000300000101525520000309400+0000000\r\n",
            "  R  SO0001720000400000101525520000316200+0000000\r\n",
            "  R  SO0001730000500000101525520000027900+0000000\r\n",
            "  R  SO0001740000600000101525522359160800-0000001\r\n",
            "  R  SO0001750000100000101525520008409300+0000000\r\n",
            "  R  SO0001760000200000101525510007283700+0000000\r\n",
            "  R  SO0001770000300000101525510000309400+0000000\r\n",
            "  R  SO0001780000400000101525510000316200+0000000\r\n",
            "  R  SO0001790000500000101525510000027900+0000000\r\n",
            "  R  SO0001800000600000101525512359160800-0000001\r\n",
            "  R  SO0001810000100000101525510008409300+0000000\r\n"
        ];
        //microgateAgent = new MicrogateAgent();

        for (String inputLine: microgateEmulatedOutput) {
            microgateAgent.processDeviceOutput(inputLine);
        }
    }
}