# clever_backend_task_5_string_json

## Задание

- Создать любой gradle проект;
- Проект должен быть совместим с java 17;
- Придерживаться GitFlow: master -> develop -> feature/fix;
- Разработать библиотеку, которая будет формировать на основе Java класса json и обратно;
- Использовать рефлексию;
- Предусмотреть возможную вложенность объектов (рекурсия);
- Реализовать кастомную аннотацию @JsonField, которая будет использоваться для задания имени поля в JSON, если оно
  отличается от имени поля в классе;
- Покрыть код unit tests (можно использовать jackson/gson);
- Использовать lombok.
---

## Реализация

### Из объекта в json

Тестовые случаи

1. На входе объект Product:

```java
public class Product {

    private UUID id;
    private String name;
    private Double price;
}
```

Получаем следующую строку json:

```json
{
  "id": "611dada2-8138-11ee-b962-0242ac120002",
  "name": "phone",
  "price": 100.0
}
```

2. На входе объект Order:

```java
public class Order {

    private UUID id;
    private List<Product> products;
    private OffsetDateTime createDate;
}
```

Получаем следующую строку json:

```json
{
  "id": "c2a5102a-80c4-11ee-b962-0242ac120002",
  "products": [
    {
      "id": "b2e10b3a-80c4-11ee-b962-0242ac120002",
      "name": "phone",
      "price": 100.0
    }
  ],
  "createDate": "2015-10-18T11:20:30.000001-05:00"
}
```

3. На входе объект Customer:

```java
public class Customer {

    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate dateBirth;
    private List<Order> orders;
}
```

Получаем следующую строку json:

```json
{
  "id": "c3323c32-80c1-11ee-b962-0242ac120002",
  "firstName": "firstName",
  "lastName": "LastName",
  "dateBirth": "2024-08-26",
  "orders": [
    {
      "id": "ba5d110e-80c1-11ee-b962-0242ac120002",
      "products": [
        {
          "id": "611dada2-8138-11ee-b962-0242ac120002",
          "name": "phone",
          "price": 100.0
        }
      ],
      "createDate": "2015-10-18T11:20:30.000001-05:00"
    },
    {
      "id": "3a53130e-80c1-11ee-b962-0242ac120002",
      "products": [
        {
          "id": "611dada2-8138-11ee-b962-0242ac120002",
          "name": "phone",
          "price": 100.0
        }
      ],
      "createDate": "2014-11-14T10:23:37.000001-03:00"
    }
  ]
}
```

4. На входе объект TestModel:

```java
public class TestModel {

  private byte byteField;
  private short shortField;
  private long longField;
  private int intField;
  private double doubleField;
  private float floatField;
  private boolean booleanField;
  private Integer integerField;
  private BigInteger bigIntegerField;
  private Product product;
  private Customer customer;
  private List<Order> orderList;
  private List<Product> productList;
}
```

Получаем следующую строку json:

```json
{
  "byteField": 10,
  "shortField": 100,
  "longField": 1000,
  "intField": 10000,
  "doubleField": 3.14,
  "floatField": 2.718,
  "booleanField": true,
  "integerField": 42,
  "bigIntegerField": 1234567,
  "product": {
    "id": "611dada2-8138-11ee-b962-0242ac120002",
    "name": "phone",
    "price": 100.0
  },
  "customer": {
    "id": "c3323c32-80c1-11ee-b962-0242ac120002",
    "firstName": "firstName",
    "lastName": "LastName",
    "dateBirth": "2024-08-26",
    "orders": [
      {
        "id": "ba5d110e-80c1-11ee-b962-0242ac120002",
        "products": [
          {
            "id": "611dada2-8138-11ee-b962-0242ac120002",
            "name": "phone",
            "price": 100.0
          }
        ],
        "createDate": "2015-10-18T11:20:30.000001-05:00"
      },
      {
        "id": "3a53130e-80c1-11ee-b962-0242ac120002",
        "products": [
          {
            "id": "611dada2-8138-11ee-b962-0242ac120002",
            "name": "phone",
            "price": 100.0
          }
        ],
        "createDate": "2014-11-14T10:23:37.000001-03:00"
      }
    ]
  },
  "orderList": [
    {
      "id": "ba5d110e-80c1-11ee-b962-0242ac120002",
      "products": [
        {
          "id": "611dada2-8138-11ee-b962-0242ac120002",
          "name": "phone",
          "price": 100.0
        }
      ],
      "createDate": "2015-10-18T11:20:30.000001-05:00"
    },
    {
      "id": "3a53130e-80c1-11ee-b962-0242ac120002",
      "products": [
        {
          "id": "611dada2-8138-11ee-b962-0242ac120002",
          "name": "phone",
          "price": 100.0
        }
      ],
      "createDate": "2014-11-14T10:23:37.000001-03:00"
    }
  ],
  "productList": [
    {
      "id": "611dada2-8138-11ee-b962-0242ac120002",
      "name": "phone",
      "price": 100.0
    }
  ]
}
```

### Из json в объект

Тестовые случаи

1. На входе объект json объекта Product:

```json
{
  "id": "611dada2-8138-11ee-b962-0242ac120002",
  "name": "phone",
  "price": 100.0
}
```

На выходе объект Product:

```
Product(id=611dada2-8138-11ee-b962-0242ac120002, name=phone, price=100.0)
```

2. На входе объект json объекта Order:

```json
{
  "id": "c2a5102a-80c4-11ee-b962-0242ac120002",
  "products": [
    {
      "id": "b2e10b3a-80c4-11ee-b962-0242ac120002",
      "name": "phone",
      "price": 100.0
    }
  ],
  "createDate": "2015-10-18T11:20:30.000001-05:00"
}
```

На выходе объект Order:

```
Order(id=c2a5102a-80c4-11ee-b962-0242ac120002, products=[Product(id=b2e10b3a-80c4-11ee-b962-0242ac120002, name=phone, price=100.0)], createDate=2015-10-18T11:20:30.000001-05:00)
```

3. На входе объект json объекта Customer:

```json
{
  "id": "c3323c32-80c1-11ee-b962-0242ac120002",
  "firstName": "firstName",
  "lastName": "LastName",
  "dateBirth": "2024-08-26",
  "orders": [
    {
      "id": "ba5d110e-80c1-11ee-b962-0242ac120002",
      "products": [
        {
          "id": "611dada2-8138-11ee-b962-0242ac120002",
          "name": "phone",
          "price": 100.0
        }
      ],
      "createDate": "2015-10-18T11:20:30.000001-05:00"
    },
    {
      "id": "3a53130e-80c1-11ee-b962-0242ac120002",
      "products": [
        {
          "id": "611dada2-8138-11ee-b962-0242ac120002",
          "name": "phone",
          "price": 100.0
        }
      ],
      "createDate": "2014-11-14T10:23:37.000001-03:00"
    }
  ]
}
```

На выходе объект Customer:

```
Customer(id=c3323c32-80c1-11ee-b962-0242ac120002, firstName=firstName, lastName=LastName, dateBirth=2024-08-26, orders=[Order(id=ba5d110e-80c1-11ee-b962-0242ac120002, products=[Product(id=611dada2-8138-11ee-b962-0242ac120002, name=phone, price=100.0)], createDate=2015-10-18T11:20:30.000001-05:00), Order(id=3a53130e-80c1-11ee-b962-0242ac120002, products=[Product(id=611dada2-8138-11ee-b962-0242ac120002, name=phone, price=100.0)], createDate=2014-11-14T10:23:37.000001-03:00)])
```

4. На входе объект json объекта TestModel:

```json
{
  "byteField": 10,
  "shortField": 100,
  "longField": 1000,
  "intField": 10000,
  "doubleField": 3.14,
  "floatField": 2.718,
  "booleanField": true,
  "integerField": 42,
  "bigIntegerField": 1234567,
  "product": {
    "id": "611dada2-8138-11ee-b962-0242ac120002",
    "name": "phone",
    "price": 100.0
  },
  "customer": {
    "id": "c3323c32-80c1-11ee-b962-0242ac120002",
    "firstName": "firstName",
    "lastName": "LastName",
    "dateBirth": "2024-08-26",
    "orders": [
      {
        "id": "ba5d110e-80c1-11ee-b962-0242ac120002",
        "products": [
          {
            "id": "611dada2-8138-11ee-b962-0242ac120002",
            "name": "phone",
            "price": 100.0
          }
        ],
        "createDate": "2015-10-18T11:20:30.000001-05:00"
      },
      {
        "id": "3a53130e-80c1-11ee-b962-0242ac120002",
        "products": [
          {
            "id": "611dada2-8138-11ee-b962-0242ac120002",
            "name": "phone",
            "price": 100.0
          }
        ],
        "createDate": "2014-11-14T10:23:37.000001-03:00"
      }
    ]
  },
  "orderList": [
    {
      "id": "ba5d110e-80c1-11ee-b962-0242ac120002",
      "products": [
        {
          "id": "611dada2-8138-11ee-b962-0242ac120002",
          "name": "phone",
          "price": 100.0
        }
      ],
      "createDate": "2015-10-18T11:20:30.000001-05:00"
    },
    {
      "id": "3a53130e-80c1-11ee-b962-0242ac120002",
      "products": [
        {
          "id": "611dada2-8138-11ee-b962-0242ac120002",
          "name": "phone",
          "price": 100.0
        }
      ],
      "createDate": "2014-11-14T10:23:37.000001-03:00"
    }
  ],
  "productList": [
    {
      "id": "611dada2-8138-11ee-b962-0242ac120002",
      "name": "phone",
      "price": 100.0
    }
  ]
}
```

На выходе объект TestModel:

```
TestModel(byteField=10, shortField=100, longField=1000, intField=10000, doubleField=3.14, floatField=2.718, booleanField=true, integerField=42, bigIntegerField=1234567, product=Product(id=611dada2-8138-11ee-b962-0242ac120002, name=phone, price=100.0), customer=Customer(id=c3323c32-80c1-11ee-b962-0242ac120002, firstName=firstName, lastName=LastName, dateBirth=2024-08-26, orders=[Order(id=ba5d110e-80c1-11ee-b962-0242ac120002, products=[Product(id=611dada2-8138-11ee-b962-0242ac120002, name=phone, price=100.0)], createDate=2015-10-18T11:20:30.000001-05:00), Order(id=3a53130e-80c1-11ee-b962-0242ac120002, products=[Product(id=611dada2-8138-11ee-b962-0242ac120002, name=phone, price=100.0)], createDate=2014-11-14T10:23:37.000001-03:00)]), orderList=[Order(id=ba5d110e-80c1-11ee-b962-0242ac120002, products=[Product(id=611dada2-8138-11ee-b962-0242ac120002, name=phone, price=100.0)], createDate=2015-10-18T11:20:30.000001-05:00), Order(id=3a53130e-80c1-11ee-b962-0242ac120002, products=[Product(id=611dada2-8138-11ee-b962-0242ac120002, name=phone, price=100.0)], createDate=2014-11-14T10:23:37.000001-03:00)], productList=[Product(id=611dada2-8138-11ee-b962-0242ac120002, name=phone, price=100.0)])
```