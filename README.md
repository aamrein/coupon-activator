# coupon-activator
The use of this software is on your own risk. I accept no liability for incorrectly redeemed coupons.

Activates your coupons from a big Swiss retailer with cloudy coupons an a big M.

The CLI program is written in java an can be uses as follow.
You can call the help with the parameter `--help`.

## Examples
### Example with username and password
Login to your account with username and password.

`java -jar yorPathToTheJar/file.jar -u username -p password`

### Example with credentials file
Login to your account with a credential file. You have to put the credentials in the first line 
separated with a double point. Example: `usernam:password`

`java -jar yorPathToTheJar/file.jar -c phatToFile` 

### Example with filter
With a type filter you can exclude coupons with the given types form the activation.

`java -jar yorPathToTheJar/file.jar -c phatToFile -tf bonus`