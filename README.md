<h2 align="center">
  <img src="https://github.com/7oSkaaa/7oSkaaa/blob/main/Images/about_me.gif?raw=true" width="45px" />
  <strong>Consulta de Operador – Automatización</strong>
</h2>

<p align="center">
Automatización avanzada en Java y Selenium que permite identificar y registrar el operador móvil de múltiples números telefónicos extraídos de un archivo Excel, optimizando procesos de consulta masiva y asegurando la precisión de los datos.
</p>

<p align="center">
  <img src="https://media.giphy.com/media/kH1DBkPNyZPOk0BxrM/giphy.gif" width="180"/>
</p>

---

## 📌 **Descripción del Proyecto**

Este proyecto automatiza la consulta del operador móvil de uno o varios números telefónicos utilizando la página DoctorSIM.

El flujo principal es:

- Leer los números desde un archivo Excel (.xlsx).

- Ingresar automáticamente los números en la página web.

- Obtener el nombre del operador correspondiente.

- Escribir el resultado en la misma hoja Excel, en la columna asignada.

💡 Funciona de forma confiable incluso al alternar redes Wi-Fi, gracias a la implementación de esperas y reconexiones automáticas.

💡 El sistema está diseñado para ser robusto, modular y fácil de mantener, optimizando el tiempo en procesos repetitivos de consulta.

---

## 🛠️ **Información del Proyecto**



|              Campo              |      Detalle       |
|:-------------------------------:|:------------------:|
|       **Versión Actual**        |       1.3.0        |
|    **Última Actualización**     | 23 de Enero, 2026  |
|          **Lenguaje**           |      Java 17+      |
| **Framework de Automatización** | Selenium WebDriver |
|   **Gestión de Dependencias**   |       Maven        |
|       **Driver Manager**        |  WebDriverManager  |
| **Lectura/Escritura de Excel**  |     Apache POI     |


---


## 🧱 Arquitectura del Proyecto

El proyecto sigue un enfoque modular, organizado por capas para mejorar mantenibilidad, escalabilidad y pruebas:


| Capa            | Paquete                         | Función                                                                                                                                                     |
| --------------- | ------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Automation**  | `com.automatizacion.automation` | Contiene la lógica principal de la automatización y la integración de todas las capas. Punto de coordinación de la ejecución completa.                      |
| **Controlador** | `com.automatizacion.controller` | Orquesta el flujo del programa, interactuando con las capas de model, service y view para ejecutar la automatización.                                       |
| **Driver**      | `com.automatizacion.driver`     | Maneja la configuración y administración de WebDriver, asegurando la conexión correcta con el navegador.                                                    |
| **Locators**    | `com.automatizacion.locators`   | Contiene todos los localizadores de Selenium (`By`), centralizando los elementos web de manera organizada.                                                  |
| **Modelo**      | `com.automatizacion.model`      | Gestiona la estructura de datos, incluyendo la lectura y escritura de archivos Excel, manipulación de números telefónicos y lectura de `config.properties`. |
| **Repository**  | `com.automatizacion.repository` | Encapsula la lógica de acceso a datos y almacenamiento temporal, sirviendo como puente entre model y service.                                               |
| **Service**     | `com.automatizacion.service`    | Contiene la lógica de negocio, como la alternancia de redes, validaciones y operaciones sobre los datos antes de enviarlos a la web.                        |
| **Vista**       | `com.automatizacion.view`       | Gestiona la presentación de información, principalmente mostrando mensajes y resultados en consola.                                                         |
| **Aplicación**  | `App.java`                      | Punto de inicio del programa.                                                                                                                               |

⚡ Esta arquitectura asegura que cada capa tenga una responsabilidad única, facilitando la incorporación de nuevas funciones sin comprometer la estabilidad.

---

## 🚀 **Tecnologías Utilizadas**
|     Tecnología     |                  Uso                  |
|:------------------:|:-------------------------------------:|
|      Java 17+      |     Lógica principal del proyecto     |
| Selenium WebDriver |       Automatización de la web        |
|     Apache POI     | Lectura y escritura de archivos Excel |
|  WebDriverManager  |     Gestión automática del driver     |
|       Maven        | Manejo de dependencias y compilación  |


---

⚡ Características Destacadas

Lectura/escritura de Excel robusta con Apache POI.

Automatización confiable con Selenium WebDriver.

Alternancia automática de redes Wi-Fi para obtener IPs diferentes.

Manejo de tiempos de espera para evitar errores de carga en la web.

Compatible con múltiples números y hojas de Excel.

---
<div align="center">
  <h3>
    <img src='https://raw.githubusercontent.com/ShahriarShafin/ShahriarShafin/main/Assets/handshake.gif' width="60px" />
  Contactame
  </h3>
</div>
<br>

</div>
<p align="center">
  <a href="mailto:sebatianpena950@gmail.com"  target="_blank">
    <img align="center" alt="TienHuynh-TN | Gmail" width="26px" src="https://github.com/SebasCodeDeveloper/SebasCodeDeveloper/blob/main/gmail.gif" />
  </a> &nbsp;&nbsp;

  <a href="https://www.linkedin.com/in/sebastian-penna-dev/" target="_blank">
    <img align="center" alt="TienHuynh-TN | Linkedin" width="43px" src="https://media3.giphy.com/media/a9eTxCdJhDU98Jp79g/giphy.gif" />
  </a> &nbsp;&nbsp;

  <a href="https://www.facebook.com/sebastian.pena.507464/" target="_blank">
      <img align="center"  width="44px" src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExNHMwbHBtODN4c3R2cTBpMGl3MmF4d3E0ZHM0emF5NWs4YzF2MWE1dSZlcD12MV9zdGlja2Vyc19zZWFyY2gmY3Q9cw/pUAgNUnRUqxyx5PsHe/giphy.gif" />
  </a> &nbsp;&nbsp;

  <a href="https://www.instagram.com/sebas.720.pdc/" target="_blank">
    <img align="center" alt="TienHuynh-TN | Instagram" width="35px" src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExM2s5ZG1qYmV1a2sybHV0eGt1ejhsNXhkc2t1OThyamozOWFzd29vMSZlcD12MV9zdGlja2Vyc19zZWFyY2gmY3Q9cw/rZAStCy2giIh7le1Gs/giphy.gif" />
  </a> &nbsp;&nbsp;

  <a href="https://github.com/SebasCodeDeveloper" target="_blank">
    <img align="center" alt="TienHuynh-TN | GitHub" width="26px" src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExZW91YjQ0eHppM2c5bmluajMyN2VhaW1xeDY5djI0YXMyYm9nYjN0aCZlcD12MV9zdGlja2Vyc19zZWFyY2gmY3Q9cw/OFEabGCcVqsckIGn8G/giphy.gif" />
  </a> &nbsp;&nbsp;  
<br><br>

<div align="center">
  :heart_eyes: Thanks for watching my profile! Have a nice day! :wink: <br/>
  &copy; 2025 Johan Sebastian Peña Ordoñez
</div> 

<div align="center"> 
  <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExZW91YjQ0eHppM2c5bmluajMyN2VhaW1xeDY5djI0YXMyYm9nYjN0aCZlcD12MV9zdGlja2Vyc19zZWFyY2gmY3Q9cw/CwTvSiWflgCGKgz5eb/giphy.gif" width="10%"/>

  <img src="https://raw.githubusercontent.com/bornmay/bornmay/Update/svg/Bottom.svg" alt="Github Stats" />
</div>

<div align="center">
 💡 La mejora continua no es una opción, es parte del código.
</div> 

---
