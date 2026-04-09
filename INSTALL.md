# Installation

## Base données

### Configuration de SQLSERVER pour accepter la connexion TCP/IP
- Ouvrir en mode administrateur `C:\Windows\SysWOW64\SQLServerManager15.msc`
- Activer le TCP/IP
- ![img.png](docs/install/img.png)
- Ouvrir `services.exe` 
- ![img.png](docs/install/services.png)
- Redemarrer le service SQLServer 

### Création et initialisation
Dans SSMS créer une base de données `bookhub` et importer le SQL [DDL.sql](./docs/database/MPD-DDL.sql)
