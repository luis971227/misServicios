# misServicios 📱💳

App Android **nativa** con Kotlin + Jetpack Compose + Material 3, dividida en **dos módulos** para los servicios de Cuba.

## Módulos

### 📱 ETECSA
Menú de gestión de la línea con sus opciones:
- **Comprar paquetes** — *pendiente de la guía* (códigos y pasos se irán pasando)
- **Guía de opciones** — *pendiente de la guía*
- Consultar saldo (`*222#`) y bonos/datos (`*222*266#`) — operativas: abren el marcador

### 💳 Transfermóvil
Menú con sus opciones:
- **Códigos USSD** — *pendiente de la lista de códigos que se pasará*
- Menú USSD `*99#` — operativa (abre el marcador)
- Portal y aplicación (enlace oficial)

Cada opción abre una **pantalla de detalle** con: descripción, código USSD (botón que abre el marcador), pasos numerados y/o enlace al portal. El botón *atrás* regresa al menú del módulo.

## Estructura del código

```
app/src/main/java/com/luis971227/miservicios/
├── MainActivity.kt            # navegación entre los 2 módulos + marcador USSD/enlaces
├── data/
│   └── Modulos.kt             # modelo de módulos y opciones (aquí se llenan las guías)
└── ui/
    ├── Comunes.kt             # PantallaModulo: menú + detalle + BackHandler
    ├── etecsa/EtecsaScreen.kt
    ├── transfermovil/TransfermovilScreen.kt
    └── theme/Theme.kt
```

Para añadir una opción nueva con su guía: agrega un `OpcionMenu(...)` a la lista de opciones del módulo en `data/Modulos.kt` (código USSD, pasos, enlace o `pendiente = true` mientras no se tenga todo).

## Tecnología

- Kotlin 2.0 · Jetpack Compose · Material 3
- Gradle 8.10.2 (Kotlin DSL + version catalog) · AGP 8.7.3
- minSdk 24 (Android 7+) · targetSdk 35

## Compilar el APK

- **Automático**: cada push/PR compila el APK debug en *Actions*; al crear un tag `v*` se publica un release con el APK adjunto.
- **Manual**: `./gradlew assembleDebug` (o Android Studio).

## Notas

- Los códigos son informativos y pueden cambiar; verificar en los portales oficiales.
- Firmar releases de Play requiere keystore propio (el CI genera builds debug).
