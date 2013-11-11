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

package com.tcay.util;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

/**
 * SlalomScoring
 * Teton Cay Group Inc. 2013
 * <p/>
 * User: allen
 * Date: 11/4/13
 * Time: 11:15 PM
 */
public class TextValidate {

    static public void digitsOnly(JTextField text, int min, int max) {
        ((AbstractDocument) text.getDocument()).setDocumentFilter(new IntegerDocumentFilter(min,max));
    }


    static public void digitsOnly(JTextField text) {
        ((AbstractDocument) text.getDocument()).setDocumentFilter(new IntegerDocumentFilter());
    }
}
