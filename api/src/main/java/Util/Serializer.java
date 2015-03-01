/*
 * Copyright (c) 2013, Ari Michael Ayvazyan
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    This product includes software developed by Ari Michael Ayvazyan.
 * 4. Neither the name of Ari Michael Ayvazyan nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY Ari Michael Ayvazyan ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Ari Michael Ayvazyan BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Beschreibung:		Bietet Funktionen um Objekte in einen Byte Array oder in einen String zu Casten und Strings und Byte arrays wieder zur�ck in Objekte zu Casten.
 * 		
 *   	Author: 			Ari Michael Ayvazyan
 * 		Versionsnummer: 	1.0
 * 		Datuum: 			4.6.2013
 */
package Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serialized and Deserializes Objects
 * @author Ari Michael Ayvazyan
 * @version 21.02.2015
 */
public class Serializer {

    /**
     * Object to string.
     *
     * @param inp the inp
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String ObjectToString(Object inp) throws IOException {
        return byteArrayToString(serialize(inp));
    }

    /**
     * String to object.
     *
     * @param inp the inp
     * @return the object
     * @throws IOException            Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException the class not found exception
     */
    public static Object StringToObject(String inp) throws IOException, ClassNotFoundException {
        return deserialize(stringToByteArray(inp));
    }

    /**
     * String to byte.
     *
     * @param str the str
     * @return the byte[]
     */
    public static byte[] stringToByteArray(String str) {
        byte[] erg = new byte[str.length()];//Ergebnis
        for (int i = 0; i < erg.length; i++) {
            erg[i] = (byte) str.charAt(i);
        }
        return erg;
    }

    /**
     * Byte to string.
     *
     * @param objekt the objekt
     * @return the string
     */
    public static String byteArrayToString(byte[] objekt) {
        String erg = "";//Ergebnis
        for (byte anObjekt : objekt) {
            erg += (char) anObjekt;
        }
        return erg;
    }

    /**
     * String to byte.
     *
     * @param str the str
     * @return the byte[]
     */
    public static byte[] IntegerStringToByte(String str) {
        byte[] erg = new byte[str.length() / 4];//Ergebnis
        for (int i = 0; i * 4 < str.length(); i++) {
            erg[i] = Byte.parseByte(str.substring(i * 4, ((i + 1) * 4)));
        }
        return erg;
    }

    /**
     * Byte to string.
     *
     * @param objekt the objekt
     * @return the string
     */
    public static String byteArrayToIntegerString(byte[] objekt) {
        String erg = "";//Ergebnis
        for (byte anObjekt : objekt) {
            erg += byteToIntegerString(anObjekt);
        }
        return erg;
    }

    public static String byteToIntegerString(byte b) {
        String s = "";

        if (b >= 0) {
            String t = "" + b;
            for (int i = 0; i < 4 - t.length(); i++) {
                s += "0";
            }
            s += t;
        } else {
            s += "-";
            String t = "" + b * (-1);
            for (int i = 0; i < 3 - t.length(); i++) {
                s += "0";
            }
            s += t;
        }
        return s;
    }

    /**
     * Serialize.
     *
     * @param obj the obj
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    /**
     * Deserialize.
     *
     * @param bytes the bytes
     * @return the object
     * @throws IOException            Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException the class not found exception
     */
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }
}