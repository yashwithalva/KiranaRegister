# KIRANA REGISTER

### Fucntional Requirements
1. **Transaction API** : An API will record transactions in different currencies. Use this reference API to perform currency conversion.
2. **Reporting API** : Develop API for generating weakly, monthly and yearly financial reports with insights like total credits, debits and netflow.
3. **User Authentication and Authorization** : Secure access with a clear distinction between authentication and authorization.
4. **API Rate Limiting** : Implements configurable rate limiting mechanism for both APIs to prevent abuse, exploring various algorithms and tools.
  - Ex: txn record API can have 10 requests/minute limit.
5. **Caching** : Cache currency conversion API response to avoid hitting API.

### Pre-requisites
1. Java 11
2. Gradle 6.x
3. Mongodb 5.x

### Spring setup
Spring version 2.7.18 is used.
Mongodb database is placed in the resources
Testing is conducted using `junittest`

### Currently working on:
1. Redis
2. Caching
3. ApiLimiting.

### Note:
1. fcm is used for sending push notifications to device.
