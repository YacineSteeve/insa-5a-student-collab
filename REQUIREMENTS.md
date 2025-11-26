Application d’entraide intelligente entre étudiants


Objectifs :

    Développer une architecture microservice complète

Objectifs :

Concevoir et développer une architecture microservice complète à l'aide de spring boot et Spring Cloudd

Ennoncé :

Dans le cadre d’une initiative de collaboration universitaire, on souhaite développer une application d’entraide intelligente entre étudiants, favorisant le partage de connaissances et la solidarité au sein des campus.

Cette plateforme permettra :

    aux étudiants en difficulté sur un sujet donné de publier des demandes d’aide,

    aux étudiants compétents et disponibles de proposer leur soutien,

    et au système d’identifier automatiquement les meilleurs profils pour chaque demande.

L’objectif final est de créer une communauté d’apprentissage pair-à-pair, en tirant parti des technologies modernes.
Exigences fonctionnelles :

Cette application doit permettre de gérer les étudiants. Chaque étudiant possède un id, nom et prénom, email, établissement, filière, compétences (liste de mots-clés), disponibilités (jours/heures), et avis sur les aides déjà effectuées.  Les étudiants peuvent s’enregistrer, se connecter et modifier leurs informations personnelles.
Un même étudiant peut être à la fois demandeur d’aide et aidant.

Chaque demande a un titre, description du besoin, des mots clés, l'étudiant demandeur, date souhaitée, et le statut (attente, en cours, réalisée, abandonnée, fermée).

Quand un demandeur d'aide dépose une demane, en se basant sur la description de son besoin et des informations des personnes potentielles qui peuvent aider, une liste de personnes recommandées est affichées (année d'étude, filière, compétences).

Travail à rélaiser :

Proposez une architecture microservice de cette application et implémentez-la.

En option, vous pouvez utiliser un ORM (mapping objet-relationnel), comme Hibernate.

L'interface graphique n'est pas requise, elle est optionnelle. 