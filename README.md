# FilmFinderAndroid

## Descripción

Film Finder Android es una aplicación Android desarrollada como parte de la práctica 2 y practica 5 de Desarrollo de Aplicaciones Móviles N en ESCOM. Permite a los usuarios buscar películas, ver sus detalles, guardar sus favoritas y gestionar cuentas de usuario. La aplicación interactúa con una API REST para obtener datos de películas y manejar la autenticación y los datos de usuario/favoritos.

## Características

*   **Búsqueda de Películas:** Busca películas por título.
*   **Detalles de Películas:** Muestra información detallada como año, director, género, sinopsis, póster, etc.
*   **Gestión de Favoritos:** Guarda y elimina películas favoritas por usuario.
*   **Autenticación:** Registro e inicio de sesión de usuarios.
*   **Gestión de Usuarios (Admin):** Los administradores pueden ver, modificar roles y eliminar usuarios.
*   **Historial de Búsqueda (Admin):** Los administradores pueden ver el historial de búsqueda de todos los usuarios.
*   **Vista de Todos los Favoritos (Admin):** Los administradores pueden ver todas las películas marcadas como favoritas por cualquier usuario.

## Tecnologías Utilizadas

*   **Lenguaje:** Kotlin
*   **UI:** Jetpack Compose
*   **Arquitectura:** MVVM (ViewModel, LiveData/StateFlow)
*   **Networking:** Retrofit (para consumir la API REST)
*   **Carga de Imágenes:** Coil
*   **Navegación:** Navigation Compose
*   **Backend (Asumido):** API REST (posiblemente Spring Boot, basado en el proyecto `practica2_crudRest_compu`)

## Configuración y Ejecución

1.  **Clonar el Repositorio:**
    ```bash
    git clone https://github.com/sammmcv/FilmFinderAndroid
    ```
2.  **Abrir en Android Studio:** Abre la carpeta donde hayas clonado el repositorio como un proyecto existente en Android Studio.
3.  **Backend:** Asegúrate de que el servicio backend esté configurado y ejecutándose. La aplicación Android necesita conectarse a él. Verifica la URL base de la API en el código.
4.  **Ejecutar la App:** Construye y ejecuta la aplicación en un emulador o dispositivo físico compatible (API 24+).

## API

*   **Películas:** La información de las películas se obtiene de [Menciona la fuente, ej. OMDB API, si aplica].
*   **Usuarios y Favoritos:** La gestión de usuarios y favoritos se realiza a través de la API REST personalizada del backend.

## Autores

*   Barros Martinez Luis Enrique
*   Cortés Velazquez Samuel