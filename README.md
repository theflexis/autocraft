# AutoCraft (Fabric 1.20.1)

Crafting ekranı (workbench veya envanterdeki 2x2) açıkken, seçtiğin itemları
tarif defteri üzerinden otomatik ve tekrarlı olarak craft eden client-side mod.

**Nasıl çalışır:** Mod, hile/anti-cheat atlatma yapmaz. Sunucuya, tarif
defterinde bir tarife tıklamışsın gibi standart bir "craft isteği" paketi
gönderir (Minecraft'ın kendi recipe-book özelliği). Bu yüzden sadece
malzemen varsa çalışır ve sunucu tarafında normal bir tarif defteri
tıklaması gibi işlenir.

## Kurulum (geliştirme / derleme)

Gerekenler:
- Java 17 (JDK)
- İnternet bağlantısı (Gradle bağımlılıkları indirecek)

Adımlar:
1. Bu klasörü bir yere aç.
2. Terminalde klasöre gir ve Gradle wrapper'ı oluştur:
   ```
   gradle wrapper --gradle-version 8.4
   ```
   (Sisteminde Gradle yoksa https://gradle.org/install/ adresinden kur,
   ya da IntelliJ IDEA ile projeyi açıp otomatik wrapper oluşturmasını sağla.)
3. Derle:
   ```
   ./gradlew build
   ```
   (Windows'ta `gradlew.bat build`)
4. Derlenen jar dosyası `build/libs/autocraft-1.0.0.jar` içinde olacak.

## Kurulum (oyunda kullanım)

1. [Fabric Loader](https://fabricmc.net/use/) 1.20.1 için kur.
2. [Fabric API](https://modrinth.com/mod/fabric-api) modunu indir (bu mod buna bağımlı).
3. Derlediğin `autocraft-1.0.0.jar` dosyasını, Fabric API ile birlikte
   `.minecraft/mods` klasörüne koy.
4. Oyunu başlat.

## Kullanım

- **`]` tuşu** (varsayılan): AutoCraft menüsünü açar (Options > Controls'tan değiştirilebilir).
- Menüde **Active**'i işaretle, **Items**'a tıklayıp craft edilmesini
  istediğin item ID'lerini ekle (örn. `minecraft:stick`, `minecraft:torch`).
- Bir crafting masası veya envanterini açtığında, malzeme varsa mod
  otomatik olarak seçili itemları craft etmeye başlar.
- İkinci bir keybind (varsayılan atanmamış) Active'i hızlıca aç/kapatmak için ayarlanabilir.

## Notlar

- Bu mod **tek oyunculu dünyalar** ve **client-side modlara izin veren
  sunucular** için tasarlandı. Sunucu kurallarını kontrol etmeden
  başkasının sunucusunda kullanma.
- Minecraft/Yarn mapping'leri sürüm güncellemeleriyle değişebilir; eğer
  derleme sırasında `CraftRequestC2SPacket` ile ilgili bir hata alırsan,
  https://linkie.shedaniel.dev üzerinden 1.20.1 Yarn mapping'lerinde bu
  sınıfın güncel adını/parametrelerini kontrol et.
