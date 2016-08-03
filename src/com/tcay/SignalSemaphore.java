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

package com.tcay;

/**
 * Created by allen on 4/5/16.
 */
public class SignalSemaphore {
        private boolean signal = false;

        public synchronized void set() {
            this.signal = true;
            this.notify();
        }

        public synchronized void release() throws InterruptedException{
            while(!this.signal) wait();
            this.signal = false;
        }
}
