// // server.js
// const express = require("express");
// const cors = require("cors");
// const fs = require("fs").promises; // Utiliser la version promise de fs
// const path = require("path");
// const { v4: uuidv4 } = require("uuid"); // Pour générer des IDs

// const app = express();
// const PORT = 3001; // Même port que json-server, ou un autre si vous préférez
// const DB_PATH = path.join(__dirname, "db.json"); // Chemin vers votre fichier db.json

// // --- Middleware ---
// app.use(cors()); // Activer CORS pour toutes les origines (ajustez si nécessaire pour la prod)
// app.use(express.json()); // Pour parser les corps de requête JSON

// // --- Chargement et Sauvegarde de la DB ---
// let dbData = {}; // Contiendra les données en mémoire

// // Fonction pour charger la base de données depuis le fichier
// async function loadDb() {
//   try {
//     console.log(`Attempting to load database from: ${DB_PATH}`);
//     const rawData = await fs.readFile(DB_PATH, "utf-8");
//     dbData = JSON.parse(rawData);
//     console.log("Database loaded successfully.");
//   } catch (error) {
//     // Si le fichier n'existe pas ou est invalide, commencer avec une structure vide
//     console.error("Error loading db.json:", error.message);
//     console.log("Starting with an empty DB structure.");
//     dbData = {
//       customers: [],
//       accounts: [],
//       transactions: [],
//       beneficiaries: [],
//     };
//     // Optionnel: Créer le fichier s'il n'existe pas
//     await saveDb();
//   }
// }

// // Fonction pour sauvegarder la base de données dans le fichier
// async function saveDb() {
//   try {
//     await fs.writeFile(DB_PATH, JSON.stringify(dbData, null, 2), "utf-8"); // null, 2 pour l'indentation
//     console.log("Database saved successfully.");
//   } catch (error) {
//     console.error("Error saving database:", error);
//     // Il faudrait une meilleure gestion ici dans une vraie app (logs, retry?)
//   }
// }

// // --- Routes API CRUD ---

// // == ACCOUNTS ==
// app.get("/accounts", (req, res) => {
//   console.log("GET /accounts");
//   res.json(dbData.accounts || []);
// });

// app.get("/accounts/:id", (req, res) => {
//   const accountId = req.params.id;
//   console.log(`GET /accounts/${accountId}`);
//   const account = (dbData.accounts || []).find((acc) => acc.id == accountId); // == car l'ID peut être nombre ou string
//   if (account) {
//     res.json(account);
//   } else {
//     res.status(404).json({ message: "Account not found" });
//   }
// });

// app.post("/accounts", async (req, res) => {
//   console.log("POST /accounts", req.body);
//   try {
//     const newAccountData = req.body;

//     // Validation minimale (ajoutez plus si nécessaire)
//     if (
//       !newAccountData.accountNumber ||
//       !newAccountData.type ||
//       !newAccountData.currency ||
//       typeof newAccountData.balance !== "number"
//     ) {
//       return res
//         .status(400)
//         .json({ message: "Missing or invalid account data" });
//     }

//     const newAccount = {
//       ...newAccountData,
//       id: uuidv4(), // Générer un nouvel ID unique
//     };

//     if (!dbData.accounts) dbData.accounts = []; // Initialiser si nécessaire
//     dbData.accounts.push(newAccount);
//     await saveDb(); // Sauvegarder les changements
//     res.status(201).json(newAccount); // Renvoyer le compte créé
//   } catch (error) {
//     console.error("Error processing POST /accounts:", error);
//     res.status(500).json({ message: "Internal Server Error" });
//   }
// });

// app.delete("/accounts/:id", async (req, res) => {
//   const accountId = req.params.id;
//   console.log(`DELETE /accounts/${accountId}`);
//   try {
//     const initialLength = (dbData.accounts || []).length;
//     dbData.accounts = (dbData.accounts || []).filter(
//       (acc) => acc.id != accountId,
//     ); // != pour string/number

//     if (dbData.accounts.length < initialLength) {
//       await saveDb(); // Sauvegarder seulement si quelque chose a été supprimé
//       console.log(`Account ${accountId} deleted.`);
//       res.status(204).send(); // 204 No Content est standard pour DELETE réussi
//     } else {
//       console.log(`Account ${accountId} not found for deletion.`);
//       res.status(404).json({ message: "Account not found" });
//     }
//   } catch (error) {
//     console.error("Error processing DELETE /accounts:", error);
//     res.status(500).json({ message: "Internal Server Error" });
//   }
// });

// // == TRANSACTIONS ==
// app.get("/transactions", (req, res) => {
//   const accountId = req.query.accountId; // Récupérer le paramètre ?accountId=...
//   console.log(`GET /transactions (accountId query: ${accountId})`);

//   let transactions = dbData.transactions || [];

//   if (accountId) {
//     // Filtrer les transactions par accountId si fourni
//     transactions = transactions.filter((tx) => tx.accountId == accountId); // == pour string/number
//     console.log(
//       `Filtered transactions for account ${accountId}: ${transactions.length} found.`,
//     );
//   }

//   res.json(transactions);
// });

// app.post("/transactions", async (req, res) => {
//   console.log("POST /transactions", req.body);
//   try {
//     const newTransactionData = req.body;

//     // Validation
//     if (
//       !newTransactionData.accountId ||
//       !newTransactionData.description ||
//       typeof newTransactionData.amount !== "number"
//     ) {
//       return res
//         .status(400)
//         .json({ message: "Missing or invalid transaction data" });
//     }
//     // Trouver le compte lié pour vérifier qu'il existe (optionnel mais bien)
//     const relatedAccount = (dbData.accounts || []).find(
//       (acc) => acc.id == newTransactionData.accountId,
//     );
//     if (!relatedAccount) {
//       return res.status(400).json({
//         message: `Account with id ${newTransactionData.accountId} not found.`,
//       });
//     }

//     const newTransaction = {
//       ...newTransactionData,
//       id: `txn-${uuidv4().substring(0, 8)}`, // Générer un ID de transaction
//       date: newTransactionData.date || new Date().toISOString(), // Utiliser date fournie ou date actuelle
//     };

//     if (!dbData.transactions) dbData.transactions = [];
//     dbData.transactions.push(newTransaction);

//     // --- Logique métier : Mettre à jour le solde du compte ---
//     // Attention : ceci est une simplification. Une vraie app calculerait
//     // le solde différemment et gérerait les erreurs/concurrence.
//     relatedAccount.balance += newTransaction.amount; // Modifie directement l'objet compte en mémoire
//     console.log(
//       `Updated balance for account ${relatedAccount.id} to ${relatedAccount.balance}`,
//     );
//     // --- Fin logique métier ---

//     await saveDb(); // Sauvegarder la transaction ET le solde mis à jour
//     res.status(201).json(newTransaction);
//   } catch (error) {
//     console.error("Error processing POST /transactions:", error);
//     res.status(500).json({ message: "Internal Server Error" });
//   }
// });

// // == CUSTOMERS & BENEFICIARIES (Similaires, GET seulement pour l'exemple) ==
// app.get("/customers", (req, res) => {
//   console.log("GET /customers");
//   res.json(dbData.customers || []);
// });
// app.get("/customers/:id", (req, res) => {
//   const custId = req.params.id;
//   console.log(`GET /customers/${custId}`);
//   const customer = (dbData.customers || []).find((c) => c.id == custId);
//   if (customer) {
//     res.json(customer);
//   } else {
//     res.status(404).json({ message: "Customer not found" });
//   }
// });
// // Pourrait avoir /customers/:customerId/accounts etc.

// app.get("/beneficiaries", (req, res) => {
//   console.log("GET /beneficiaries");
//   const customerId = req.query.customerId;
//   let beneficiaries = dbData.beneficiaries || [];
//   if (customerId) {
//     beneficiaries = beneficiaries.filter((b) => b.customerId == customerId);
//   }
//   res.json(beneficiaries);
// });

// // --- Démarrage du serveur ---
// // Charger la DB avant de démarrer l'écoute
// loadDb()
//   .then(() => {
//     app.listen(PORT, () => {
//       console.log(
//         `CBS Backend (Node/Express) listening on http://localhost:${PORT}`,
//       );
//       console.log(`Serving data from: ${DB_PATH}`);
//     });
//   })
//   .catch((err) => {
//     console.error("Failed to initialize server:", err);
//     process.exit(1);
//   });

///2 eme version
// server.js
const express = require("express");
const cors = require("cors");
const fs = require("fs").promises;
const path = require("path");
const { v4: uuidv4 } = require("uuid");

const app = express();
const PORT = 3001;
const DB_PATH = path.join(__dirname, "db.json");

app.use(cors());
app.use(express.json());

// --- Chargement et Sauvegarde de la DB ---
let dbData = {};

async function loadDb() {
  try {
    console.log(`Attempting to load database from: ${DB_PATH}`);
    const rawData = await fs.readFile(DB_PATH, "utf-8");
    dbData = JSON.parse(rawData);
    console.log("Database loaded successfully.");
  } catch (error) {
    console.error("Error loading db.json:", error.message);
    console.log("Starting with an empty DB structure.");
    dbData = {
      customers: [],
      accounts: [],
      transactions: [],
      beneficiaries: [],
    };
    await saveDb();
  }
}

async function saveDb() {
  try {
    await fs.writeFile(DB_PATH, JSON.stringify(dbData, null, 2), "utf-8");
    console.log("Database saved successfully.");
  } catch (error) {
    console.error("Error saving database:", error);
  }
}

// --- Générateur de Routes CRUD ---
function generateCrudRoutes(entityConfig) {
  const router = express.Router();
  const {
    name, // Nom de l'entité (ex: 'accounts')
    validationRules, // Fonction de validation
    generateId, // Fonction de génération d'ID
    businessLogic, // Logique métier supplémentaire (optionnel)
    filters, // Filtres pour GET (optionnel)
  } = entityConfig;

  // S'assurer que l'entité existe dans dbData
  if (!dbData[name]) dbData[name] = [];

  // GET ALL (avec filtres optionnels)
  router.get("/", (req, res) => {
    console.log(`GET /${name}`);

    let entities = dbData[name] || [];

    // Appliquer les filtres si définis
    if (filters) {
      for (const filterKey of Object.keys(filters)) {
        const filterValue = req.query[filterKey];
        if (filterValue && typeof filters[filterKey] === "function") {
          entities = filters[filterKey](entities, filterValue);
        }
      }
    }

    res.json(entities);
  });

  // GET BY ID
  router.get("/:id", (req, res) => {
    const entityId = req.params.id;
    console.log(`GET /${name}/${entityId}`);

    const entity = (dbData[name] || []).find((e) => e.id == entityId);

    if (entity) {
      res.json(entity);
    } else {
      res.status(404).json({ message: `${name.slice(0, -1)} not found` });
    }
  });

  // POST (CREATE)
  router.post("/", async (req, res) => {
    console.log(`POST /${name}`, req.body);

    try {
      const newEntityData = req.body;

      // Validation
      if (validationRules && typeof validationRules === "function") {
        const validationError = validationRules(newEntityData);
        if (validationError) {
          return res.status(400).json({ message: validationError });
        }
      }

      // Création de la nouvelle entité avec ID généré
      const newEntity = {
        ...newEntityData,
        id: generateId(newEntityData),
      };

      // Ajout à la base de données
      if (!dbData[name]) dbData[name] = [];
      dbData[name].push(newEntity);

      // Exécution de la logique métier si définie
      if (businessLogic && typeof businessLogic.afterCreate === "function") {
        await businessLogic.afterCreate(newEntity, dbData);
      }

      await saveDb();
      res.status(201).json(newEntity);
    } catch (error) {
      console.error(`Error processing POST /${name}:`, error);
      res.status(500).json({ message: "Internal Server Error" });
    }
  });

  // PUT (UPDATE)
  router.put("/:id", async (req, res) => {
    const entityId = req.params.id;
    console.log(`PUT /${name}/${entityId}`, req.body);

    try {
      const updateData = req.body;
      const entityIndex = (dbData[name] || []).findIndex(
        (e) => e.id == entityId,
      );

      if (entityIndex === -1) {
        return res
          .status(404)
          .json({ message: `${name.slice(0, -1)} not found` });
      }

      // Validation
      if (validationRules && typeof validationRules === "function") {
        const validationError = validationRules(updateData);
        if (validationError) {
          return res.status(400).json({ message: validationError });
        }
      }

      const oldEntity = dbData[name][entityIndex];
      const updatedEntity = {
        ...oldEntity,
        ...updateData,
        id: entityId, // S'assurer que l'ID reste le même
      };

      dbData[name][entityIndex] = updatedEntity;

      // Exécution de la logique métier si définie
      if (businessLogic && typeof businessLogic.afterUpdate === "function") {
        await businessLogic.afterUpdate(updatedEntity, oldEntity, dbData);
      }

      await saveDb();
      res.json(updatedEntity);
    } catch (error) {
      console.error(`Error processing PUT /${name}/${entityId}:`, error);
      res.status(500).json({ message: "Internal Server Error" });
    }
  });

  // DELETE
  router.delete("/:id", async (req, res) => {
    const entityId = req.params.id;
    console.log(`DELETE /${name}/${entityId}`);

    try {
      const entityIndex = (dbData[name] || []).findIndex(
        (e) => e.id == entityId,
      );

      if (entityIndex === -1) {
        return res
          .status(404)
          .json({ message: `${name.slice(0, -1)} not found` });
      }

      const deletedEntity = dbData[name][entityIndex];
      dbData[name].splice(entityIndex, 1);

      // Exécution de la logique métier si définie
      if (businessLogic && typeof businessLogic.afterDelete === "function") {
        await businessLogic.afterDelete(deletedEntity, dbData);
      }

      await saveDb();
      res.status(204).send();
    } catch (error) {
      console.error(`Error processing DELETE /${name}/${entityId}:`, error);
      res.status(500).json({ message: "Internal Server Error" });
    }
  });

  return router;
}

// --- Configuration des entités ---
const entityConfigs = {
  accounts: {
    name: "accounts",
    validationRules: (data) => {
      if (
        !data.accountNumber ||
        !data.type ||
        !data.currency ||
        typeof data.balance !== "number"
      ) {
        return "Missing or invalid account data";
      }
      return null; // Pas d'erreur
    },
    generateId: () => uuidv4(),
    businessLogic: {
      // Pas de logique spéciale pour accounts
    },
  },

  transactions: {
    name: "transactions",
    validationRules: (data) => {
      if (
        !data.accountId ||
        !data.description ||
        typeof data.amount !== "number"
      ) {
        return "Missing or invalid transaction data";
      }
      return null;
    },
    generateId: () => `txn-${uuidv4().substring(0, 8)}`,
    businessLogic: {
      afterCreate: async (transaction, db) => {
        // Mise à jour du solde du compte
        const relatedAccount = (db.accounts || []).find(
          (acc) => acc.id == transaction.accountId,
        );
        if (relatedAccount) {
          relatedAccount.balance += transaction.amount;
          console.log(
            `Updated balance for account ${relatedAccount.id} to ${relatedAccount.balance}`,
          );
        }
      },
    },
    filters: {
      accountId: (transactions, accountId) => {
        return transactions.filter((tx) => tx.accountId == accountId);
      },
    },
  },

  customers: {
    name: "customers",
    validationRules: (data) => {
      if (!data.firstName || !data.lastName || !data.email) {
        return "Missing or invalid customer data";
      }
      return null;
    },
    generateId: () => `cust-${uuidv4().substring(0, 8)}`,
  },

  beneficiaries: {
    name: "beneficiaries",
    validationRules: (data) => {
      if (!data.customerId || !data.beneficiaryName || !data.accountNumber) {
        return "Missing or invalid beneficiary data";
      }
      return null;
    },
    generateId: () => `bene-${uuidv4().substring(0, 8)}`,
    filters: {
      customerId: (beneficiaries, customerId) => {
        return beneficiaries.filter((b) => b.customerId == customerId);
      },
    },
  },
};

// --- Montage des routes ---
loadDb()
  .then(() => {
    // Pour chaque configuration d'entité, générer les routes CRUD
    Object.values(entityConfigs).forEach((config) => {
      app.use(`/${config.name}`, generateCrudRoutes(config));
    });

    // Route spéciale pour récupérer les comptes d'un client
    app.get("/customers/:customerId/accounts", (req, res) => {
      const customerId = req.params.customerId;
      console.log(`GET /customers/${customerId}/accounts`);

      const customerAccounts = (dbData.accounts || []).filter(
        (acc) => acc.customerId == customerId,
      );
      res.json(customerAccounts);
    });

    // Démarrage du serveur
    app.listen(PORT, () => {
      console.log(
        `Generic CRUD API Server listening on http://localhost:${PORT}`,
      );
      console.log(`Serving data from: ${DB_PATH}`);
    });
  })
  .catch((err) => {
    console.error("Failed to initialize server:", err);
    process.exit(1);
  });
