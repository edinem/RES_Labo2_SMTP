## RES - Laboratoire #2 - SMTP

#### Auteurs: Edin Mujkanovic et Daniel Paiva

#### Date:  10 avril 2019



##### 1. Introduction

Durant ce labaratoire, nous avons du implémenter un client SMTP qui se connectait à un serveur SMTP afin d'envoyer des mails à des personnes spécifiques. Ces derniers sont située dans des groupes, qui contient également un attaquant. L'attaquant représente la personne qui va envoyer les mails aux victimes. Un attaquant est composé d'un prénom, un nom de famille et d'une adresse mail.

##### 2. Mise en place du serveur SMTP mock

Afin de mettre en place un serveur SMTP mock, il suffit d'aller dans le dossier **docker** situé à la racine et d'exécuter le script **start-mock-server.sh**. Ce dernier va tirer l'image du docker, et lancer un container de cette dernière.



##### 3. Clear and simple instructions for configuring your tools and running a prank campaign

Les configurations du logiciel se trouve dans un seul fichier nommé *config.xml*, ce dernier se trouve à la racine du projet. Plusieurs éléments se trouvent dans ce fichier : 

- Balise <server>: 

  Cette dernière contient les informations du serveur SMTP que le client va utiliser afin d'envoyer les mails. Le serveur est composé de : 

  - une balise <username> qui spécifie le nom d'utilisateur à envoyer au serveur SMTP afin de s'identifier. **<u>Si aucune authentification est nécessaire, il faut laisser ce champs vide.</u>**
  - une balise <password> qui spécifie le mot de passe à envoyer au serveur SMTP afin de s'identifier. **<u>Si aucune authentification est nécessaire, il faut laisser ce champs vide.</u>**
  - une balise <ip> qui spécifie l'adresse du serveur SMTP. Nous pouvons également utiliser un nom de domaine.
  - une balise <port> qui spécifie le port du serveur SMTP.

- Balise <groups>:

  Cette dernière contient tous les groupes définit par l'utilisateur. Elle est donc composée de balises <group> qui elles définissent :

  - Une balise <fakemail> qui définit l'expéditeur du mail, qui lui est défini par : 
    - une balise <firstname> qui contient le prénom à afficher pour l'exepediteur.
    - une balise <lastname> qui contient le nom à afficher pour l'expediteur.
    - une balise <mail> qui contient le mail à afficher pour l'expediteur.
  - Une balise <victims> qui contient plusieurs <victim> qui contiennent les adresses mail des destinataires. 

- Balise <messages>:

  Cette dernière est composée de plusieurs <message>, qui eux sont composés de :

  - une balise <sujet> qui contient le sujet du mail.
  - une balise <text> qui contient le texte contenu dans le corps du mail.



Afin de créer votre propre attaque, il vous suffit de modifier les informations concernant votre serveur, créer de groupes de personnes composés d'un attaquant, et de une ou plusieurs victimes. Le contenu de vos mails sera choisi aléatoirement parmi tous les messages que vous aurez prédéfinis.

```xml
<config>
    <server>
        <username></username>
        <password></password>
        <ip>127.0.0.1</ip>
        <port>2525</port>
    </server>
    <groups>
        <group>
            <fakemail>
                <firstname>Donald</firstname>
                <lastname>Trump</lastname>
                <mail>donald@trump.com</mail>
            </fakemail>
            <victims>
                <victim>john@doe.com</victim>
                <victim>jean@rie.com</victim>
            </victims>
        </group>
        <group>
            <fakemail>
                <firstname>Mc</firstname>
                <lastname>Donald</lastname>
                <mail>ronald@donald.com</mail>
            </fakemail>
            <victims>
                <victim>john@doe.com</victim>
                <victim>jean@rie.com</victim>
            </victims>
        </group>
    </groups>
    <messages>
        <message>
            <subject>You won !</subject>
            <text>Click here to win </text>
        </message>
        <message>
            <subject>You won !</subject>
            <text>Click here to win an iPhone XS Max 512GB !</text>
        </message>
        <message>
            <subject>You won !</subject>
            <text>Click here to win an iPhone XS Max 512GB !</text>
            </text>
        </message>
    </messages>
</config>
```



##### 4. A description of your implementation

