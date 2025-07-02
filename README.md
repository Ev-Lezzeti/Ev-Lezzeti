
# 🍲 EvLezzeti - Ev Mutfağından Dijital Pazara

**EvLezzeti**, geleneksel Türk ev yemeklerini dijital dünyaya taşıyan, Kotlin ve MVVM mimarisi kullanılarak geliştirilen bir mobil yemek siparişi uygulamasıdır. Bu proje, Erciyes Üniversitesi Bilgisayar Mühendisliği Bölümü bitirme tezi kapsamında geliştirilmiştir.

> 🌐 GitHub: [EvLezzeti GitHub Reposu](https://github.com/Ev-Lezzeti/Ev-Lezzeti)

## 📱 Uygulama Hakkında

EvLezzeti, ev hanımlarının kendi hazırladıkları yemekleri dijital pazarda sunmasına olanak sağlayan bir platformdur. Kullanıcılar sağlıklı, güvenilir ve ev yapımı yemekleri mobil cihazları üzerinden kolayca sipariş verebilir.

### 🎯 Proje Amaçları

- Ev yemeklerine kolay erişim sağlamak
- Kadın girişimciliğini ve istihdamı desteklemek
- Geleneksel yemek kültürünü korumak
- Sağlıklı beslenme alışkanlıklarını yaymak
- Sürdürülebilir dijital çözümler geliştirmek

## 🧹 Kullanılan Teknolojiler

| Katman               | Teknoloji / Araçlar                        |
| -------------------- | ------------------------------------------ |
| Programlama Dili     | Kotlin                                     |
| Mimarî Yapı          | MVVM (Model-View-ViewModel)                |
| Arka Uç              | Firebase Firestore, Firebase Auth, Storage |
| Dependency Injection | Hilt                                       |
| UI                   | XML, Material Design, ViewBinding          |
| Navigation           | Jetpack Navigation Component               |
| Animasyonlar         | Lottie JSON, XML Transition Animations     |

## 🏠 Uygulama Mimarisi

EvLezzeti, katmanlı yazılım mimarisi ile modüler bir şekilde geliştirilmiştir:


UI Layer (Fragments, Adapters)
    ↓
ViewModel Layer (State management)
    ↓
Repository Layer (Veri soyutlama)
    ↓
DataSource Layer (Firebase Firestore, Storage)


### 📦 Paket Yapısı


- ui/
    - fragment/
    - adapter/
    - viewmodel/
- data/
    - datasource/
    - entity/
    - repo/
- di/
    - AppModule.kt
- util/
    - Extension.kt
- res/
    - layout/
    - values/
    - navigation/
    - anim/
    - font/


## 🔐 Kimlik Doğrulama

- Firebase Authentication ile kullanıcı girişi
  - 📧 Email/Şifre
  - 🔐 Google Login
- Email doğrulama
- Güvenli şifreleme

## ☁️ Veri Tabanı

- Firestore NoSQL yapısı

  - Kullanıcılar
  - Yemekler
  - Siparişler
  - Kategoriler

- Firebase Storage

  - Görsellerin URL referanslı saklanması

## 🎨 Uygulama Arayüzü Örnekleri

Aşağıdaki görseller projeye ait posterden alınmıştır ve temel kullanıcı akışı şunlardır:

| Ekran Adı         | Açıklama                                                    |
| ----------------- | ----------------------------------------------------------- |
| Splash Ekranı     | Uygulama logosu ve giriş animasyonu                         |
| Giriş Ekranı      | Kullanıcının e-posta/şifre ile veya Google hesabıyla girişi |
| Tanıtım Ekranları | Uygulama özelliklerinin ilk kez kullanıcıya tanıtımı        |
| Mutfak Ekranı     | Ev hanımlarının sunduğu yemeklerin listelendiği alan        |
| Sepet Ekranı      | Seçilen yemeklerin listelendiği, ödeme öncesi özet sayfası  |
| Konum Ekranı      | Kullanıcının harita üzerinden adres belirlediği ekran       |
| Ödeme Ekranı      | Kartla veya kapıda ödeme seçenekleriyle sipariş tamamlama   |
| Profil Ekranı     | Kullanıcı bilgileri, geçmiş siparişler ve adres yönetimi    |

## 🧭 Kullanıcı Yol Haritası

1. Kullanıcı uygulamayı açar → Splash ekranı gösterilir.
2. İlk kez kullanıcıysa tanıtım ekranları gösterilir → ardından giriş ekranı.
3. Giriş/kayıt sonrası kullanıcı anasayfaya yönlendirilir.
4. Mutfaklardan yemek seçilir → sepete eklenir.
5. Sepetten adres ve ödeme adımları tamamlanır.
6. Sipariş onaylanır ve geçmiş siparişlere yansıtılır.


## 🛠️ Geliştirici Ekip

👨‍💻 **Ömer Faruk DİDİN**\
👨‍💻 **Mustafa Umut İDAM**\
🎓 Erciyes Üniversitesi - Bilgisayar Mühendisliği (2025)

📘 Bitirme Projesi Danışmanı:\
👩‍🏫 Arş. Gör. Fatma AZİZOĞLU


---

**EvLezzeti**: Sağlıklı yemekler, güçlü kadınlar, dijital çözümler.

