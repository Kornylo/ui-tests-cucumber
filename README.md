**How to use?**

To start the tests, you need to run the command:

**mvn test -DsuiteXmlFile={Test_Suite} -Dhost={Target_Host} -Dselenoid={Selenoid_Address} -Dbrowser={Browser_Name}**

_Commands Example:_

_Running code on Linux / OS X_
```
 mvn test  -Dcucumber.options="--tags @C12600 --plugin io.qameta.allure.cucumberjvm.AllureCucumberJvm" -Dhost=http://carid.com -Dbrowser=chrome
```

