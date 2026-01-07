# 🔐 Restrict User Access to a Specific Package in SAP Integration Suite (CPI)

This guide describes how to give a third-party user access to **only one specific integration package** in SAP Integration Suite, allowing them to develop interfaces within that package and restricting access to others.

---

## 🛠️ Objective

Grant a user access to:
- Only one specific package in SAP Integration Suite.
- Allow development (Read/Write access) within that package.
- Prevent access to all other packages or deployed artifacts.

---

## ✅ Supported SAP Approach

SAP supports this use case using **Authorization Groups** and **Role Collections** in **SAP BTP Cockpit**.

---

## 📌 Implementation Steps

### 1. Create an Authorization Group for the Package
- Go to **SAP Integration Suite > Design > Packages**.
- Open or create the required package.
- Set the **Authorization Group** field to a unique identifier:

  ```
  THIRD_PARTY_DEV
  ```

This group acts as a tag for access control.

---

### 2. Create a Custom Role Collection in BTP Cockpit
- Navigate to: `BTP Cockpit > Subaccount > Security > Role Collections`
- Click **Create Role Collection**
- Name it: `ThirdPartyDevAccess` (or similar)

---

### 3. Assign Scoped Role to the Role Collection
- Add the predefined role: `IntegrationDeveloper`
- Set the scope to `ReadWrite`
- Add an attribute filter for the authorization group:

```json
{
  "name": "IntegrationDeveloper",
  "scope": "ReadWrite",
  "attributes": {
    "AuthorizationGroup": "THIRD_PARTY_DEV"
  }
}
```

This ensures access is limited to only packages tagged with the matching authorization group.

---

### 4. Assign the Role Collection to the External User
- Go to: `Security > Trust Configuration`
- Locate the third-party user (by email or SAML ID)
- Assign the role collection `ThirdPartyDevAccess` to the user.

---

## 🧪 Test the Setup

Use a test user account to verify:
- Only the tagged package is visible and editable.
- No access to other packages or deployed artifacts.

---

## 📘 Example Package Configuration

| Package Name        | Authorization Group |
|---------------------|---------------------|
| OrdersProcessing    | INTERNAL_DEV        |
| PartnerAPI          | THIRD_PARTY_DEV     |
| CRM_Sync            | INTERNAL_DEV        |

A user with `AuthorizationGroup: THIRD_PARTY_DEV` will only see and access `PartnerAPI`.

---

## 🔐 Security Recommendations

- Do **not** assign wildcard `*` authorization group roles to third-party users.
- Use **Secure Store** in CPI for credential handling.
- Apply **OAuth2, Basic Auth, or Certificate Auth** based on integration requirements.

---

## 📚 References

- 📖 [SAP Help: Authorization Management](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/authorization-management?locale=en-IN)
- 📺 [OutOfTheBoxEA YouTube Tutorials](https://www.youtube.com/@OutOfTheBoxEA)
