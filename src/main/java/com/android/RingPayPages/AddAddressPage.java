package com.android.RingPayPages;

import org.openqa.selenium.By;

public class AddAddressPage {

	//Add CurrentAddress header
	public static By objAddCurrentAddressHeader = By.xpath("//*[@text='Add Current Address']");
	
	//Room Flat No
	public static By objFlatNo = By.xpath("//*[@text='Room/Flat No. & Building Name']/parent::android.view.ViewGroup/following-sibling::android.widget.EditText");
	
	//Address line 1
	public static By objAddressLine1 = By.xpath("//*[@text='Address line 1']/parent::android.view.ViewGroup/following-sibling::android.widget.EditText");
	
	//Landmark
	public static By objLandmark = By.xpath("//*[@text='Landmark']/parent::android.view.ViewGroup/following-sibling::android.widget.EditText");
	
	//pincode
	public static By objPinCode = By.xpath("//*[@text='Pincode']/parent::android.view.ViewGroup/following-sibling::android.widget.EditText");
		
	//Submit Button
	public static By objSubmitBtn = By.xpath("//*[@text='Submit']");
	
	public static By objAddressHeader=By.xpath("//*[contains(@text,'Add Current Address')]");
    public static By objRoomNoTextField=By.xpath("//*[contains(@text,'Room/Flat No. & Building Name')]");
    public static By objRoomNoField = By.xpath("//*[contains(@text,'Room/Flat No. & Building Name')]/parent::android.view.ViewGroup/following-sibling::android.widget.EditText");
    
    public static By objAddressLineOneTextField=By.xpath("//*[contains(@text,'Address line 1')]");
    public static By objAddressLineOneField = By.xpath("//*[contains(@text,'Address line 1')]/parent::android.view.ViewGroup/following-sibling::android.widget.EditText");
    
    public static By objAddressLineTwoTextField=By.xpath("//*[contains(@text,'Address line 2 (Optional)')]");
    public static By objAddressLineTwoField = By.xpath("//*[contains(@text,'Address line 2 (Optional)')]/parent::android.view.ViewGroup/following-sibling::android.widget.EditText");
    
    public static By objLandmarkTextField=By.xpath("//*[contains(@text,'Landmark')]");
    public static By objLandmarkTextFields=By.xpath("//*[contains(@text,'Landmark')]/parent::android.view.ViewGroup/following-sibling::android.widget.EditText");
    
    public static By objPincodeTextField=By.xpath("//*[contains(@text,'Pincode')]");
    public static By objPinCodeField = By.xpath("//*[contains(@text,'Pincode')]/parent::android.view.ViewGroup/following-sibling::android.widget.EditText");
    
    public static By objCityTextField=By.xpath("//*[contains(@text,'City')]");
    public static By objStateTextField=By.xpath("//*[contains(@text,'State')]");
    public static By objSubmitButton=By.xpath("//*[contains(@text,'Submit')]");
    public static By objSpclCharWarnMessage=By.xpath("//*[contains(@text,'Only following special characters are allowed - , & /')]");
    public static By objMandatoryWarnMessage=By.xpath("//*[contains(@text,'Mandatory field.')]");
    public static By objMinThreeCharWarnMessage=By.xpath("//*[contains(@text,'Minimum 3 character. Only following special characters are allowed - , & /')]");
    public static By objSixDigitWarnMessage=By.xpath("//*[contains(@text,'Please enter 6 digit pin code')]");
    public static By objInvalidPinCode=By.xpath("//*[contains(@text,'Invalid Pin Code')]");
}
