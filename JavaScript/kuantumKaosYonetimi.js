const readline = require('readline');

// ==================== CUSTOM EXCEPTION ====================
class KuantumCokusuException extends Error {
    constructor(nesneId) {
        super(`KUANTUM ÇÖKÜŞÜ! Nesne ID: ${nesneId} patladı!`);
        this.name = 'KuantumCokusuException';
        this.nesneId = nesneId;
    }
}

// ==================== ABSTRACT CLASS ====================
class KuantumNesnesi {
    #stabilite;
    #tehlikeSeviyesi;

    constructor(id, stabilite, tehlikeSeviyesi) {
        if (new.target === KuantumNesnesi) {
            throw new Error('KuantumNesnesi soyut sınıftır, doğrudan örneklenemez!');
        }
        this.id = id;
        this.stabilite = stabilite;
        this.tehlikeSeviyesi = tehlikeSeviyesi;
    }

    get stabilite() {
        return this.#stabilite;
    }

    set stabilite(value) {
        if (value > 100) {
            this.#stabilite = 100;
        } else {
            this.#stabilite = value; // 0'ın altına düşebilir, exception fırlatılacak
        }
    }

    get tehlikeSeviyesi() {
        return this.#tehlikeSeviyesi;
    }

    set tehlikeSeviyesi(value) {
        if (value < 1) {
            this.#tehlikeSeviyesi = 1;
        } else if (value > 10) {
            this.#tehlikeSeviyesi = 10;
        } else {
            this.#tehlikeSeviyesi = value;
        }
    }

    // Abstract method - must be implemented by subclasses
    analizEt() {
        throw new Error('analizEt() metodu alt sınıfta implement edilmelidir!');
    }

    durumBilgisi() {
        return `[${this.id}] Stabilite: %${this.stabilite.toFixed(1)} | Tehlike: ${this.tehlikeSeviyesi}/10`;
    }

    stabiliteKontrol() {
        if (this.stabilite <= 0) {
            throw new KuantumCokusuException(this.id);
        }
    }

    // Check if object implements IKritik interface
    isKritik() {
        return typeof this.acilDurumSogutmasi === 'function';
    }
}

// ==================== CONCRETE CLASSES ====================
class VeriPaketi extends KuantumNesnesi {
    constructor(id, stabilite) {
        super(id, stabilite, 2);
    }

    analizEt() {
        console.log('Veri içeriği okundu.');
        this.stabilite -= 5;
        this.stabiliteKontrol();
    }

    durumBilgisi() {
        return super.durumBilgisi() + ' [VeriPaketi - Güvenli]';
    }
}

class KaranlikMadde extends KuantumNesnesi {
    constructor(id, stabilite) {
        super(id, stabilite, 7);
    }

    analizEt() {
        console.log('Karanlık madde analiz ediliyor... Dikkatli olun!');
        this.stabilite -= 15;
        this.stabiliteKontrol();
    }

    // IKritik interface implementation
    acilDurumSogutmasi() {
        this.stabilite += 50;
        console.log(`[${this.id}] Acil soğutma uygulandı! Yeni stabilite: %${this.stabilite.toFixed(1)}`);
    }

    durumBilgisi() {
        return super.durumBilgisi() + ' [KaranlıkMadde - TEHLİKELİ!]';
    }
}

class AntiMadde extends KuantumNesnesi {
    constructor(id, stabilite) {
        super(id, stabilite, 10);
    }

    analizEt() {
        console.log('⚠️ EVRENİN DOKUSU TİTRİYOR... ⚠️');
        this.stabilite -= 25;
        this.stabiliteKontrol();
    }

    // IKritik interface implementation
    acilDurumSogutmasi() {
        this.stabilite += 50;
        console.log(`[${this.id}] ACİL soğutma uygulandı! Yeni stabilite: %${this.stabilite.toFixed(1)}`);
    }

    durumBilgisi() {
        return super.durumBilgisi() + ' [AntiMadde - ÇOK TEHLİKELİ!!!]';
    }
}

// ==================== MAIN PROGRAM ====================
class KuantumAmbarYonetimi {
    constructor() {
        this.envanter = [];
        this.nesneCounter = 1;
        this.rl = readline.createInterface({
            input: process.stdin,
            output: process.stdout
        });
    }

    soru(mesaj) {
        return new Promise((resolve) => {
            this.rl.question(mesaj, (cevap) => {
                resolve(cevap.trim());
            });
        });
    }

    menuGoster() {
        console.log('\n════════════════════════════════════════');
        console.log('    KUANTUM AMBARI KONTROL PANELİ');
        console.log('════════════════════════════════════════');
        console.log('1. Yeni Nesne Ekle');
        console.log('2. Tüm Envanteri Listele (Durum Raporu)');
        console.log('3. Nesneyi Analiz Et');
        console.log('4. Acil Durum Soğutması Yap');
        console.log('5. Çıkış');
        console.log('════════════════════════════════════════');
    }

    yeniNesneEkle() {
        const tip = Math.floor(Math.random() * 3) + 1;
        const stabilite = Math.floor(Math.random() * 51) + 50; // 50-100 arası
        const id = `QN-${String(this.nesneCounter++).padStart(4, '0')}`;

        let yeniNesne;

        switch (tip) {
            case 1:
                yeniNesne = new VeriPaketi(id, stabilite);
                console.log(`\n✅ Yeni VeriPaketi eklendi: ${id} (Stabilite: %${stabilite})`);
                break;
            case 2:
                yeniNesne = new KaranlikMadde(id, stabilite);
                console.log(`\n⚠️ Yeni KaranlıkMadde eklendi: ${id} (Stabilite: %${stabilite})`);
                break;
            default:
                yeniNesne = new AntiMadde(id, stabilite);
                console.log(`\n🔴 Yeni AntiMadde eklendi: ${id} (Stabilite: %${stabilite})`);
                break;
        }

        this.envanter.push(yeniNesne);
    }

    envanteriListele() {
        if (this.envanter.length === 0) {
            console.log('\n📦 Envanter boş. Henüz nesne eklenmedi.');
            return;
        }

        console.log('\n╔══════════════════════════════════════════════════════════╗');
        console.log('║                    ENVANTER RAPORU                        ║');
        console.log('╚══════════════════════════════════════════════════════════╝');

        for (const nesne of this.envanter) {
            console.log(nesne.durumBilgisi());
        }
    }

    nesneBul(nesneId) {
        return this.envanter.find(n => n.id.toLowerCase() === nesneId.toLowerCase());
    }

    async nesneAnalizEt() {
        if (this.envanter.length === 0) {
            console.log('\n📦 Envanter boş. Analiz edilecek nesne yok.');
            return;
        }

        const nesneId = await this.soru('\nAnaliz edilecek nesnenin ID\'sini girin: ');
        const nesne = this.nesneBul(nesneId);

        if (!nesne) {
            console.log(`\n❌ '${nesneId}' ID'li nesne bulunamadı!`);
            return;
        }

        console.log(`\n🔬 ${nesneId} analiz ediliyor...`);
        nesne.analizEt();
        console.log(`Analiz tamamlandı. Yeni stabilite: %${nesne.stabilite.toFixed(1)}`);
    }

    async acilSogutmaYap() {
        if (this.envanter.length === 0) {
            console.log('\n📦 Envanter boş. Soğutulacak nesne yok.');
            return;
        }

        const nesneId = await this.soru('\nSoğutulacak nesnenin ID\'sini girin: ');
        const nesne = this.nesneBul(nesneId);

        if (!nesne) {
            console.log(`\n❌ '${nesneId}' ID'li nesne bulunamadı!`);
            return;
        }

        // Type checking - check if object has acilDurumSogutmasi method (implements IKritik)
        if (nesne.isKritik()) {
            nesne.acilDurumSogutmasi();
        } else {
            console.log(`\n❌ Bu nesne soğutulamaz! '${nesneId}' kritik bir nesne değil.`);
        }
    }

    async calistir() {
        console.log('╔══════════════════════════════════════════════════════════╗');
        console.log('║     OMEGA SEKTÖRÜ - KUANTUM VERİ AMBARI                  ║');
        console.log('║     Hoş geldiniz, Vardiya Amiri!                         ║');
        console.log('╚══════════════════════════════════════════════════════════╝');

        try {
            while (true) {
                this.menuGoster();
                const secim = await this.soru('Seçiminiz: ');

                switch (secim) {
                    case '1':
                        this.yeniNesneEkle();
                        break;
                    case '2':
                        this.envanteriListele();
                        break;
                    case '3':
                        await this.nesneAnalizEt();
                        break;
                    case '4':
                        await this.acilSogutmaYap();
                        break;
                    case '5':
                        console.log('\nVardiya sona erdi. Güle güle!');
                        this.rl.close();
                        return;
                    default:
                        console.log('\n❌ Geçersiz seçim! Lütfen 1-5 arası bir sayı girin.');
                        break;
                }
            }
        } catch (error) {
            if (error instanceof KuantumCokusuException) {
                console.log('\n╔══════════════════════════════════════════════════════════╗');
                console.log('║  💥💥💥 SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR... 💥💥💥    ║');
                console.log('╚══════════════════════════════════════════════════════════╝');
                console.log(`\n${error.message}`);
                console.log('\n[GAME OVER]');
                this.rl.close();
            } else {
                throw error;
            }
        }
    }
}

// ==================== ENTRY POINT ====================
const uygulama = new KuantumAmbarYonetimi();
uygulama.calistir();