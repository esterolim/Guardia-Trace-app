# ** Guardian Trace – Development Plan (English)**

## **1. Project Overview**

* Guardian Trace is a mobile application designed to help users collect, secure, and export evidence
  of online harassment or stalking.
* Built with **Kotlin**, **Jetpack Compose**, **Encrypted Room**, and **MVVM + Clean Architecture**.
* Includes **SOS system**, **stealth mode**, and **emergency capture**.
* Focused on **digital safety**, **privacy**, and **data protection**.

---

# **2. Development Phases**

---

## **Phase 1 — Requirements & Architecture**

### **2.1 Functional Requirements**

* Collect text, screenshots, audio, links, and timestamps.
* Store all evidence encrypted locally.
* Provide quick **Emergency Capture** (1–2 taps).
* Provide **SOS mode** to alert trusted contacts.
* Provide **Stealth Mode** (app disguised or hidden).
* Allow exporting evidence as PDF/ZIP.
* Authentication: PIN or biometric.
* Full offline capability.

### **2.2**

* High security: encrypted Room + encrypted shared prefs.
* Clean Architecture + MVVM + State/StateConverter.
* All UI isolated from ViewModel.
* Dark/stealth-safe color scheme (optional).
* Fast performance under stress.

### **2.3 Architecture Definition**

* **Presentation Layer**

    * Composables (UI)
    * UI State
    * State Converter
    * ViewModel (never accessing UI directly)
* **Domain Layer**

    * Use Cases (EvidenceCapture, ExportEvidence, ActivateSOS, etc.)
    * Domain Models
    * Business rules
* **Data Layer**

    * Repositories + implementations
    * Encrypted Room + DAOs
    * Data encryption + secure storage
