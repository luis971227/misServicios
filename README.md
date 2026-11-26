# misServicios 📱💳

App Android nativa con **Kotlin + Jetpack Compose + Material 3** que agrupa los servicios de **ETECSA** y **Transfermóvil** (Cuba), con registro local de gastos.

## Funciones

- 🏠 **Inicio**: resumen de gastos del mes y acceso rápido.
- 📱 **ETECSA**: consulta de saldo y bonos por código USSD (botón que abre el marcador), recargas y planes desde el portal oficial.
- 💳 **Transfermóvil**: menú USSD \*99\#, pagos de servicios, transferencias, consultas de tarjeta y enlaces oficiales.
- 🧾 **Gastos**: registro local (sin backend) de recargas, pagos y transferencias, con categorías, historial y totales. Los datos se guardan en el teléfono.

## Tecnología

- Kotlin 2.0 + Jetpack Compose + Material 3 (UI declarativa, sin XML de layouts)
- Gradle 8.10.2 con Kotlin DSL y *version catalog* (`gradle/libs.versions.toml`)
- AGP 8.7.3 · compileSdk/targetSdk 35 · minSdk 24 (Android 7+)
- Persistencia simple con `SharedPreferences` + JSON (sin dependencias extra)

## Compilar el APK

### En GitHub Actions (automático)

Cada push a `main` (o PR) compila el APK debug y lo publica como **artefacto** en la pestaña *Actions* (`misServicios-debug-apk`). Al crear un tag `v*` (por ejemplo `v1.0.0`) se publica además como **release**.

### En tu PC

1. Abre el proyecto en **Android Studio** (Ladybug o más nuevo).
2. `Run ▶ misServicios` o `Build > Build APK`.

Por terminal:

```bash
./gradlew assembleDebug
./gradlew assembleRelease
```

## Notas

- Los códigos USSD y enlaces son informativos: verifícalos en los portales oficiales.
- Firmar releases de Play requiere un keystore propio (el CI genera builds debug).
- Los adaptadores para integraciones futuras (APIs reales) se pueden conectar en `app/src/main/java/com/luis971227/miservicios/data/`.
