#Challenge Meli
_Para coordinar acciones de respuesta ante fraudes, es útil tener disponible información
contextual del lugar de origen detectado en el momento de comprar, buscar y pagar. Para
ello se decide crear una herramienta que dada una IP obtenga información asociada.
El ejercicio consiste en construir una API Rest que permita:_
1. Dada una dirección IP, encontrar el país al que pertenece y mostrar:
   a. El nombre y código ISO del país
   b. Moneda local y su cotización actual en dólares o euros.
2. Ban/Blacklist de una IP: marcar la ip en una lista negra no permitiéndole consultar el
   la información del punto 1._

### Pre-requisitos 📋

Antes de poder ejecutar el software se debe con contar con:

| Gradle | Docker-compose | Docker | Java |
|---|---|---|---|
| 7.3.3 | 2.2.1 | 20.10.11 | 13 |

### Ejecución 🔧

_Se deben ejecutar los siguientes comandos en la terminal_

_Ejecutar los test_

```
gradle clean test --info
```

_Generar el archivo ".jar"_

```
gradle clean build
```

_Generar el contenedor del Dockerfile_

```
docker-compose build
```

_Ejecutar los contenedores_

```
docker-compose up
```


##Endpoints

[Swagger](http://localhost:8080/swagger-ui.html)
- POST ```/api/blacklist``` Bloquea las ip
- POST ```/api/country/info``` Retorna información completa de las ip
- POST ```/api/internal/clearMemoryCache```  Limpia todas las caches de memoria
- GET ```/api/ips/saved``` Retorna todas los country info de las ip persistidas
- GET ```/api/statistics``` Retorna las estadisticas
