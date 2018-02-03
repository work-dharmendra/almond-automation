package com.dhar.automation.common;

import com.dhar.automation.RunConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dharmendra.singh
 */
public class MacroUtil {

    public static String substituteMacro(String value, RunConfig runConfig){
        String result = value;
        try {
            Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
            Matcher m = p.matcher(value);
            while(m.find()) {
                String macro = m.group(1);
                //TODO some better way to replace macros

                //check if macro is predefined
                boolean isPreDefinedMacro = false;
                for(MacroType macroType : MacroType.values()){
                    if(macro.toLowerCase().equals(macroType.toString().toLowerCase())){
                        isPreDefinedMacro = true;
                        result = result.replace("${" + macro + "}", getMacroValue(macroType));
                    }
                }
                if (!isPreDefinedMacro) {
                    String macroDefaultArray [] = macro.split(":");
                    if(macroDefaultArray.length == 1){
                        if(macro.contains(":")){
                            //if user enter ${name:}, then macroDefaultArray returns only one string
                            if (runConfig.getVariableValue(macroDefaultArray[0]) != null) {
                                if (runConfig.getVariableValue(macro) != null) {
                                    result = result.replace("${" + macro + "}", runConfig.getVariableValue(macro));
                                }
                            } else {
                                result = result.replace("${" + macro + "}", "");
                            }
                        } else {
                            if (runConfig.getVariableValue(macro) != null) {
                                result = result.replace("${" + macro + "}", runConfig.getVariableValue(macro));
                            }
                        }

                    } else {
                        if(runConfig.getVariableValue(macroDefaultArray[0]) != null){
                            result = result.replace("${" + macro + "}", runConfig.getVariableValue(macroDefaultArray[0]));
                        } else {
                            String macroValue = macro.substring(macro.indexOf(":") + 1, macro.length());
                            result = result.replace("${" + macro + "}", macroValue);
                        }

                    }

                }
                /*
                //check if there is any parameter in runConfig.params
                if(runConfig.getParams().containsKey(macro)){
                    result = runConfig.getParams().get(macro);
                } else {
                    //check if macro is predefined
                    boolean isPreDefinedMacro = false;
                    for(MacroType macroType : MacroType.values()){
                        if(macro.toLowerCase().equals(macroType.toString().toLowerCase())){
                            isPreDefinedMacro = true;
                            result = value.replace("${" + macro + "}", getMacroValue(macroType));
                        }
                    }
                    if (!isPreDefinedMacro) {
                        result = value.replace("${" + macro + "}", runConfig.getVariableValue(macro));
                    }
                }*/

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getMacroValue(MacroType macroType){
        String result = null;

        if(macroType == MacroType.TIMESTAMP){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
            result = sdf.format(new Date());
        }

        return result;
    }
}
