/*
 * AccountBean.java
 *
 * Created on February 28 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * Used to store characteristics for account. Called by AccountBean.
 */

package com.splwg.selfservice;

import java.util.*;
import java.text.*;

import org.dom4j.Element;


public class CharacteristicUtilities {

        private List _List;

        private String _CharacteristicTypeFieldName;
        private String _CharacteristicValueFieldName;
        private String _AdHocCharacteristicValueFieldName;
        private String _EffectiveDateFieldName;
        private String _AdHocFieldName;
        private Properties _Properties;

        /** Creates new CharacteristicUtilities instance */
        public CharacteristicUtilities(
            List list,
            String characteristicTypeFieldName,
            String characteristicValueFieldName,
            String adHocCharacteristicValueFieldName,
            String effectiveDateFieldName,
            String adHocFieldName,
            Properties properties) {

            setList(list);

            setCharacteristicTypeFieldName(characteristicTypeFieldName);
            setCharacteristicValueFieldName(characteristicValueFieldName);
            setAdHocCharacteristicValueFieldName(adHocCharacteristicValueFieldName);
            setEffectiveDateFieldName(effectiveDateFieldName);
            setAdHocFieldName(adHocFieldName);
            setProperties(properties);
        }//CharacteristicUtilities

        private List getList() {
            return _List;
        }//end getList

        private void setList(List newValue) {
            _List = newValue;
        }//end setList

        private String getCharacteristicTypeFieldName() {
            return _CharacteristicTypeFieldName;
        }//end getCharacteristicTypeFieldName

        private void setCharacteristicTypeFieldName(String newValue) {
            _CharacteristicTypeFieldName = newValue;
        }//end setCharacteristicTypeFieldName

        private String getCharacteristicValueFieldName() {
            return _CharacteristicValueFieldName;
        }//end getCharacteristicValueFieldName

        private void setCharacteristicValueFieldName(String newValue) {
            _CharacteristicValueFieldName = newValue;
        }//end setCharacteristicValueFieldName

        private String getAdHocCharacteristicValueFieldName() {
            return _AdHocCharacteristicValueFieldName;
        }//end getAdHocCharacteristicValueFieldName

        private void setAdHocCharacteristicValueFieldName(String newValue) {
            _AdHocCharacteristicValueFieldName = newValue;
        }//end setAdHocCharacteristicValueFieldName

        private String getEffectiveDateFieldName() {
            return _EffectiveDateFieldName;
        }//end getEffectiveDateFieldName

        private void setEffectiveDateFieldName(String newValue) {
            _EffectiveDateFieldName = newValue;
        }//end setEffectiveDateFieldName

        private String getAdHocFieldName() {
            return _AdHocFieldName;
        }//end getAdHocFieldName

        private void setAdHocFieldName(String newValue) {
            _AdHocFieldName = newValue;
        }//end setAdHocFieldName

        private Properties getProperties() {
            return _Properties;
        }//end getProperties

        private void setProperties(Properties newValue) {
            _Properties = newValue;
        }//end setProperties

        public HashMap getCharacteristics() throws ParseException {

            HashMap characteristics = new HashMap();
            HashMap effectiveDates = new HashMap();

            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                getProperties().getProperty(
                "com.splwg.selfservice.XAIDateFormat"
                ));

            for (Iterator iter = getList().iterator(); iter.hasNext();) {
                Element ele = (Element) iter.next();
                String charType = Util.getValue(ele, getCharacteristicTypeFieldName());
                Date effDate = sdf.parse(Util.getValue(ele, getEffectiveDateFieldName()));
                String charValue;
                if (Util.getValue(ele, getAdHocFieldName()).equals("ADV")) {
                    charValue = Util.getValue(ele, getAdHocCharacteristicValueFieldName());
                } else {
                    charValue = Util.getValue(ele, getCharacteristicValueFieldName());
                } //end if

                if (!today.before(effDate)) { // ignore characteristics that are not yet in effect
                    Date prevEffDate = (Date) effectiveDates.get(charType);
                    if (prevEffDate == null || prevEffDate.before(effDate)) {
                        characteristics.put(charType, charValue);
                        effectiveDates.put(charType, effDate);
                    } //end if
                } //end if
            }
            return characteristics;
        } //getCharacteristics
}//CharacteristicUtilities

