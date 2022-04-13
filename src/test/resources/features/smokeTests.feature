Feature: smokeProd

  @smokeProd @prod @C12600 @smokeProdMobile
  Scenario: Check posibility use any payment method for anonymous user with US distant shipping
    When Go to 'Home' page
    When Get random Product from list
    When Fill out Form on Current page:
      | email       | smoktest@test.test |
      | s_firstname | Test               |
      | s_lastname  | Man                |
      | s_address   | testAddress        |
      | s_zipcode   | 00801              |
      | s_phone     | +49 30 847123731   |
    And Select 'Virgin Islands' by Visible Text in 's_state' Drop Down List
    And Press on '//*[@class="cart-order"]//*[text()='Checkout as Guest']' Button By Xpath .*
    Then Check 'Payment Details' Text on Current page
    And Switch to 'checkout' iFrame
    And Press on 'fullcardnum' Button By ID
    And Enter '5555555555554444' text in field form by id 'fullcardnum' with replace space
    And Select '12 (Dec)' by Visible Text in 'edm' Drop Down List
    And Select '2029' by Visible Text in 'edy' Drop Down List
    And Enter '123' text by id 'cvv2' Field
    And Go to 'Cart' page
    And Press on '(//button[contains(@class,'gpay-button black')])[2]' Button By Xpath "GPay"
    And Wait 5 Second
    And Refresh Current Page
    And Press on '(//a[contains(@class,'item -paypal')])[2]' Button By Xpath "Paypal Checkout"
    And Wait 5 Second
    And Press on '(//a[contains(@class,'item -affirm-monthly')])[2]' Button By Xpath "Affirm Monthly Payments"