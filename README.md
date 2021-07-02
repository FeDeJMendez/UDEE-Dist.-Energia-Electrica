UDEE - UTN Distribucion de Energia Electrica
=============

Models
-------------

![](https://github.com/Amoruso221/udee/blob/main/DER.png)

Endpoints
-------------

### localhost:8080/api
	
#### /backoffice

 | /addresses| | | 
 |  ------------- |  ------------- |  ------------- | 
 |  POST | / | AddNew | 
 |  PUT | /{id} | Edit | 
 |  GET | / | GetAll | 
 |  DELETE | /{id} | Delete | 

 |/bills| | | 
 |  ------------- |  ------------- |  ------------- | 
 | GET | /generate | CreateAll |
 | GET | / | GetAll |
 | GET | /{id} | GetById |
 | GET | /addresses/debt/{idAddress} | AddressDebt |
 | GET | /clients/debt/{idClient} | ClientDebt |
 | DELETE | /{id} | Delete |

 |/clients| | | 
 |  ------------- |  ------------- |  ------------- | 
 | POST | / | AddNew |
 | PUT | /{dni} | Edit |
 | GET | /	| GetAll |
 | GET | /topten/{start}/{end} | TenMoreConsumersByDateTimeRange |
 | DELETE | /{id} | Delete |

 |/measurements| | | 
 |  ------------- |  ------------- |  ------------- | 
 | POST | / | AddNew |
 | GET | /	| GetAll |
 | GET | /{id} | GetById |
 | GET | /addresses/{idAddress}/{start}/{end} | GetByAddressAndDateTimeRange |
 | DELETE | /{id} | Delete |

 |/meters| | | 
 |  ------------- |  ------------- |  ------------- | 
 | POST | / | AddNew |
 | GET | / | GetAll |
 | GET | /{serialNumber} | GetById |
 | DELETE | /{serialNumber} | Delete |
 | PUT | /{serialNumber} | Edit |
 | PUT | /{serialNumber}/addresses/{id} | AddAddressToMeter |

 |/rates| | | 
 |  ------------- |  ------------- |  ------------- | 
 | POST | / | AddNew |
 | GET | /	| GetAll |
 | GET | /{id} | GetById |
 | DELETE | /{id} | Delete |
 | PUT | /{dni} | Edit |

#### /client
 |/bills| | | 
 |  ------------- |  ------------- |  ------------- | 
 | GET | /dates/{start}/{end} | GetBillsBetweenDates |
 | GET | /unpaid | GetUnpaidBills |

 |/measurements| | | 
 |  ------------- |  ------------- |  ------------- | 
 | GET | /consumption/{start}/{end} | TotalKwhAndAmountBetweenDates |
 | GET | /{start}/{end} | GetBetweenDates |

#### /
 | | | |
 |  ------------- |  ------------- |  ------------- | 
 | POST | /login | Login |




