# Dependencies
Java 1.8 and Maven

# Usage
1. Start the services with the provided repo and run `docker-compose up`
2. Install dependencies via `mvn install`
3. Execute tests with: `mvn test`

# Bugs found

1. The Price Engine returns prices for Products that do not exist. 
First, delete all the documents in the MongoDB Collection Products. Then, Try the following request: `curl -X GET "http://localhost:3000/product/2340923402394" -H  "accept: application/json" -H  "accept-language: en-us"`
You should receive a message for product not existing, it still returns a price. I am assuming the PriceEngine returns hardcoded values. The rest assured test is currently failing to reflect this assumption. 

2. The POST product endpoint accepts payloads with product id == null.  You should not be allowed to post products with a null id. The rest assured test is currently failing to reflect this assumption.
To reproduce try this request: `curl -X POST "http://localhost:3001/product" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{  \"id\": null,  \"name\": \"string\",  \"description\": \"string\"}"`

3.  The PUT product endpoint accepts a non existant product to update. It returns the same payload and no evident error message.
Reproduce it by trying the following request:  `curl -X PUT "http://localhost:3001/product/909080980808098" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{  \"id\": \"string\",  \"name\": \"string\",  \"description\": \"string\"}"`
You should receive an error message stating that the product you are trying to update does not exist. The rest assured test is currently failing to reflect this assumption.
                                                                                                      
4. A couple of comments on the documentation found on `http://localhost:3001/api-docs/swagger/#/Product/deleteUser`:
* The description says it "Creates a product in the database", should read "Delete a product in the database". Very dangeruous!
* The url is deleteUser, a little misleading as well. 


# Possible improvements to the project

* Make the MongoDB connection a Singleton
* Close the MongoDB connection after the tests end
* Add data providers for each test instead of directly loading json files for test data
* Create more test cases around long strings, different formats other than UTF-8, lists of values passed as parameters
* Validate that the operations are being impacted in the MongoDB collections in an end to end bfashion
* Create performance tests with k6.io to validate response of the service with load and add to CI/CD
* Add Contract Tests for this APIS and its consumers to automatically test the contracts
* Atomic commits being done instead of a big bang commit in git

# Possible Performance Improvements for the API

* Use compression techniques like gzip, data is being transmitted uncompressed
* Use more performant json serializers in the project
* Use cache techniques
