/*
 * MenuItem.java
 *
 * Created in December 2002, by Michel Ben Oliel
 * Part of CorDaptix Web Self Service (10321)
 * Interfance for Authentication class plug in.  (DummyAuthentication.java is its implementation.)
 * Used for administering the menu. Called by Menu.jsp.
 */

package com.splwg.selfservice;

public class MenuItem {

    //~ Instance fields --------------------------------------------------------------------------------------

    private String _ItemKey;
    private String _ItemDescription;
    private String _ItemURL;
    private String _ItemLocation;

    //~ Constructors -----------------------------------------------------------------------------------------

    /** Creates new MenuItem instance */
    public MenuItem(
                    String itemKey,
                    String itemDescription,
                    String itemURL,
                    String itemLocation) {
        setItemKey(itemKey);
        setItemDescription(itemDescription);
        setItemURL(itemURL);
        setItemLocation(itemLocation);
    } //MenuItem

    public MenuItem(
                    String itemKey,
                    String itemDescription,
                    String itemURL) {
        setItemKey(itemKey);
        setItemDescription(itemDescription);
        setItemURL(itemURL);
        setItemLocation("local");
    } //MenuItem

    //~ Methods ----------------------------------------------------------------------------------------------

    public String getItemKey() {
        return _ItemKey;
    } //end getItemKey

    private void setItemKey(String newValue) {
        _ItemKey = newValue;
    } //end setItemKey

    public String getItemDescription() {
        return _ItemDescription;
    } //end getItemDescription

    private void setItemDescription(String newValue) {
        _ItemDescription = newValue;
    } //end setItemDescription

    public String getItemURL() {
        return _ItemURL;
    } //end getItemURL

    private void setItemURL(String newValue) {
        _ItemURL = newValue;
    } //end setItemURL

    public String getItemLocation() {
        return _ItemLocation;
    } //end getItemLocation

    private void setItemLocation(String newValue) {
        _ItemLocation = newValue;
    } //end setItemLocation
} //MenuItem
