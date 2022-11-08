package com.android.RingPayPages;

import org.openqa.selenium.By;

public class PromoCodeOfferPage {

//  Offer Text
	public static By objOffer = By.xpath("//*[@text='Offer']");
//  Approval Limit Amount
	public static By objApprovalLimitAmount = By.xpath("(//*[@text='You are eligible for approved limit of']/following-sibling::android.widget.TextView)[1]");
// Fee Details
	public static By objFeeDetails = By.xpath("//*[@text='Fee Details']");
//  Approval Limit Text
	public static By objApprovalLimitText = By.xpath("//*[@text='You are eligible for approved limit of']");
//  Check Box
	public static By objCheckBox = By.xpath("//*[@class='android.widget.CheckBox']");
//  TermsAndCondition
	public static By objTermsAndCondition = By
			.xpath("(//*[@class='android.widget.CheckBox']/following-sibling::android.widget.TextView)[1]");
//  Accept Offer Text
	public static By objAcceptOffer = By.xpath("//*[contains(@text,'Accept')]");
	
	//Upi Id
	public static By objUpiIdMerchant = By.xpath("(//*[@text='Amount']/following-sibling::android.widget.TextView)[3]");
	
	//Amount
	public static By objAmount = By.xpath("(//*[@text='Amount']/following-sibling::android.widget.TextView)[1]");

	//Repayment Date Text
	public static By objRepaymentDateText = By.xpath("(//*[@text='Amount']/following-sibling::android.widget.TextView)[4]");
	
	//Repayment date
	public static By objrepaymentDate = By.xpath("(//*[@text='Amount']/following-sibling::android.widget.TextView)[5]");
}
