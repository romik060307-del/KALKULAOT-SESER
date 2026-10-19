# RK Calculator Vault (Android)

Aplikasi Android ringan dengan tampilan kalkulator biasa dan ruang file pribadi lokal. Nama aplikasi di launcher: **Kalkulator**. Aplikasi tidak memerlukan akun, server, atau izin internet. Kode UI memakai widget Android bawaan tanpa library UI tambahan.

## Fitur

- Kalkulator standar: tambah, kurang, kali, bagi, persen, desimal, tanda negatif, dan tanda kurung melalui ekspresi.
- Ruang pribadi untuk foto, video, dan berkas umum.
- Impor dari pemilih file Android dan ekspor kembali ke lokasi yang dipilih.
- File dan metadata dienkripsi dengan AES-GCM; kunci utama dibungkus dengan PIN melalui PBKDF2. Pratinjau video memakai file sementara di cache privat aplikasi dan akan dibersihkan saat ditutup atau aplikasi dibuka kembali.
- File yang dihapus masuk ke Sampah lebih dahulu; bisa dipulihkan atau dihapus permanen.
- Pratinjau foto dan video di dalam aplikasi.
- Ganti PIN, pencarian file, filter foto/video/file, dan perlindungan screenshot saat ruang pribadi terbuka.
- Pengelola aplikasi untuk menyembunyikan atau memulihkan aplikasi dari daftar RK Home, serta membuka aplikasi tersembunyi dari dalam brankas.
- Launcher opsional RK Home yang ringan, tanpa pustaka UI pihak ketiga.

## Cara masuk tanpa tulisan sandi di layar kalkulator

Perilaku masuk sengaja tidak menampilkan tulisan "masukkan sandi".

1. Saat baru dipasang, pilih kode angka 4–12 digit; disarankan 6–10 digit dan hindari kode yang mudah ditebak.
2. Ketik angka itu pada kalkulator lalu tekan `=` **dua kali berturut-turut tanpa mengubah ekspresi**. Ini akan menyimpan kode dan membuka ruang pribadi. Tidak perlu menekan tombol `+` untuk masuk.
3. Pada pemakaian berikutnya, ketik kode yang sama dan tekan `=` dua kali berturut-turut.
4. Kode juga bisa berupa hasil operasi matematika yang menghasilkan angka bulat 4–12 digit. Yang dipakai sebagai kode adalah hasilnya, bukan tanda operasinya. Tombol `+` tetap tersedia untuk perhitungan biasa.
5. Tekan **Kalkulator** atau tombol Kembali untuk keluar dari ruang pribadi. Saat aplikasi masuk ke latar belakang, ruang pribadi dikunci kembali.

Penting: karena antarmukanya sengaja tetap seperti kalkulator, aplikasi tidak menampilkan panduan PIN pada layar awal. Simpan kode di tempat aman; tidak ada mekanisme reset PIN yang dapat membuka data terenkripsi tanpa kode lama.

## Penyimpanan dan batasan

- File disimpan pada direktori privat internal aplikasi, bukan di file APK itu sendiri. File tidak ditulis sebagai foto/video biasa ke Galeri. File hanya keluar dari ruang pribadi jika pengguna memilih **Ekspor ke HP**.
- Android menghapus data privat aplikasi jika data aplikasi dibersihkan atau aplikasi di-uninstall. Ekspor/cadangkan file penting sebelum menghapus aplikasi. Jika PIN terlupa, file terenkripsi tidak bisa dipulihkan.
- Fitur sembunyikan aplikasi bekerja di launcher **RK Home**. Buka ruang pribadi → **Aplikasi** → tekan **Sembunyikan** pada aplikasi yang dipilih. Lalu tekan **Pilih RK Home sebagai Beranda** dan pilih RK Home di Setelan Android. Aplikasi tersembunyi bisa dibuka dari tombol **Buka** di daftar itu, atau dikembalikan dengan tombol **Pulihkan**.
- Android tidak memberi aplikasi biasa izin untuk menghapus ikon aplikasi lain dari launcher bawaan. Karena itu, ikon hanya disaring dari beranda/laci aplikasi milik RK Home. Daftar aplikasi di Pengaturan Android dan launcher lain masih dapat menampilkan aplikasi tersebut. Untuk kembali ke launcher lama, pilih **Ganti Beranda** di RK Home atau lewat Setelan Android > Aplikasi default > Aplikasi Beranda.
- Ini proyek Android APK, bukan aplikasi iPhone/iOS.
- Versi saat ini dibuat sebagai fondasi awal. Perangkat dan provider pemilih berkas yang berbeda dapat berperilaku berbeda; uji dengan salinan file non-penting terlebih dahulu.

## Membangun APK lewat GitHub Actions

Workflow ada di file terpisah: `.github/workflows/build-apk.yml`.

1. Buat repository GitHub baru.
2. Unggah **isi folder proyek ini** ke root repository. Pastikan `.github/workflows/build-apk.yml` tetap berada pada jalur persis itu.
3. Buka tab **Actions** lalu pilih **Build RK Calculator APK**. Workflow juga berjalan otomatis saat push ke `main` atau `master`.
4. Setelah berstatus berhasil, buka run terbaru dan unduh artifact **RK-Calculator-APK**. Di dalamnya ada `app-debug.apk` untuk uji coba pada perangkat Android.

Untuk build lokal, buka folder ini di Android Studio versi yang mendukung Android Gradle Plugin 8.7.3, JDK 17, dan Android SDK 35.

## Struktur penting

```text
app/
  src/main/AndroidManifest.xml
  src/main/java/com/rk/calculatorvault/MainActivity.kt
  src/main/java/com/rk/calculatorvault/LauncherActivity.kt
.github/
  workflows/
    build-apk.yml
build.gradle.kts
settings.gradle.kts
```
