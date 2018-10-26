/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.utilitaire;

import static java.lang.System.in;

/**
 *
 * @author ADA-MALICK
 */
public class Validator {

    public static  boolean isBeginLettre(String a){
        int aAsci =(int)a.charAt(0);
        if(aAsci<=65 && aAsci>=90){
            return true;
        }
        return false;
    }
    public static boolean isBeginNumber(String a){
        int aAsci =(int)a.charAt(0);
        if(aAsci<=48 && aAsci>=57 || aAsci<=97 && aAsci>=122 ){
            return true;
        }
        return false;
    }
    public static boolean isEmail(String email){
        
        if(isBeginLettre(email)){
                char [] tbC =email.toCharArray();
               for(int i=1; i<email.length();i++){
                    if(tbC[i]=='@') return true;
               }
           }
        return false;
    }
    public static  boolean isSpecialChar(String a){
        int aAsci =(int)a.charAt(0);
        if(aAsci<=65 && aAsci>=90){
            return true;
        }
        return false;
    }
    
    
    
    
}
