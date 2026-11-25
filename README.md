# AUKIF SMART GUARD AI — Android project (alpha)

Fondasi awal aplikasi Android untuk menguji koneksi RTSP kamera Franwell BQ5.

## Status saat ini

- Kerangka aplikasi Kotlin tersedia.
- Tema gelap dengan aksen hijau.
- Tombol untuk membuka stream RTSP.
- URL awal: `rtsp://192.168.100.14/live/ch00_1`.
- **Belum ada AI Human Detection, snapshot otomatis, atau layanan pemantauan latar belakang.**
- Build debug belum diuji pada perangkat fisik.

## Build APK melalui GitHub Actions

1. Ekstrak ZIP proyek ini di perangkat atau komputer.
2. Buat repository GitHub baru, misalnya `aukif-smart-guard-ai`.
3. Unggah seluruh isi folder proyek (termasuk folder tersembunyi `.github/workflows`) ke repository, pada branch `main`.
4. Buka tab **Actions** pada repository. Workflow **Build Android APK** akan berjalan setelah file di-push. Bisa juga pilih workflow lalu tekan **Run workflow**.
5. Setelah selesai dengan status hijau, buka hasil workflow dan unduh artifact `AUKIF-SMART-GUARD-AI-debug-apk`.
6. Ekstrak artifact dan pasang `app-debug.apk` di HP Android. Android mungkin meminta izin memasang aplikasi dari sumber tersebut.

Build cloud memerlukan akun GitHub. Workflow ini menggunakan runner GitHub-hosted dan menghasilkan APK debug, bukan APK rilis bertanda tangan untuk distribusi publik.

## Pengujian kamera

HP harus terhubung ke Wi-Fi rumah yang sama dengan kamera. RTSP memakai alamat LAN dan tidak dapat diakses langsung dari jaringan seluler:

`rtsp://192.168.100.14/live/ch00_1`

Jangan membuka port RTSP tanpa autentikasi ke internet.

## Teknologi

- Kotlin
- Android Gradle Plugin 8.7.3
- Kotlin Gradle Plugin 2.0.21
- AndroidX Media3 RTSP
- JDK 17 / Gradle 8.9
