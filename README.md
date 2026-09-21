# Lab 7 — App con navegación

Proyecto Android (Kotlin + Jetpack Compose) del laboratorio 7: una app de personajes
de Rick & Morty con navegación entre pantallas usando destinations anotados con
`@Serializable`.

## Pantallas

- **Login** — logo de Rick & Morty y botón "Entrar". Al navegar a Characters, Login
  se elimina del back stack (si se presiona "back" en Characters, la app se cierra).
- **Characters** — listado (`LazyColumn`) de personajes con nombre, especie, status,
  género e imagen (cargada con Coil). Al tocar un personaje se navega a su detalle
  enviando únicamente su `id`.
- **Character details** — detalle del personaje seleccionado, con botón de back.

## Stack

- Jetpack Compose + Material3
- Navigation Compose con destinations `@Serializable` (sin argumentos por string)
- Coil para carga de imágenes desde internet
- Un solo `Activity`; toda la navegación ocurre entre composables

## Cómo compilar

```bash
./gradlew assembleDebug
```

El APK generado queda en `app/build/outputs/apk/debug/app-debug.apk`.
