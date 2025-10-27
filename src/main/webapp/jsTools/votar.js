class Votacion {
    constructor() {
        // Verifica perfil
        const stored = sessionStorage.getItem('voterProfile');
        if (!stored) {
            alert('Primero complete su perfil de votante.');
            window.location.href = 'perfilvotante.html';
            return;
        }
        this.voter = JSON.parse(stored);
        this.selectedNames = JSON.parse(sessionStorage.getItem('votedNames')) || [];
        this.selectedCard = null;
        this.logoBase64 = '';
        this.init();
    }

    init() {
        this.preloadLogo();
        this.showProfileInfo();
        this.setupInitialState();
        this.bindEvents();
    }

    showProfileInfo() {
        // Mostrar Apellido primero y luego Nombre
        $('#displayNombre').text((this.voter.apellido || '') + ' ' + this.voter.nombre);
        $('#displayCoduni').text(this.voter.coduni);
        $('#displayEscuela').text(this.voter.escuela);
    }

    preloadLogo() {
        fetch('imagen/Logo1.png')
                .then(res => res.blob())
                .then(blob => new Promise(resolve => {
                        const fr = new FileReader();
                        fr.onloadend = () => resolve(fr.result);
                        fr.readAsDataURL(blob);
                    }))
                .then(dataUrl => {
                    this.logoBase64 = dataUrl;
                })
                .catch(() => console.warn('Error cargando logo.'));
    }

    disableAllVoting() {
        $('.btn-select').each(function () {
            const txt = $(this).text();
            $(this)
                    .prop('disabled', true)
                    .text(txt === 'Votado' ? 'Votado' : 'Cerrado');
        });
    }

    setupInitialState() {
        if (this.selectedNames.length >= 3) {
            this.disableAllVoting();
        } else {
            this.selectedNames.forEach(name => {
                $('.card-candidato')
                        .filter((_, card) => $(card).data('name') === name)
                        .addClass('card-selected')
                        .find('.btn-select')
                        .text('Votado')
                        .prop('disabled', true);
            });
        }
    }

    bindEvents() {
        $('.btn-select').on('click', e => this.onSelect(e));
        $('#confirmVoteBtn').on('click', () => this.onConfirm());
    }

    onSelect(e) {
        this.selectedCard = $(e.currentTarget).closest('.card-candidato');
        const name = this.selectedCard.data('name');
        $('#candidateName').text(name);
        $('#confirmModal').modal('show');
    }

    onConfirm() {
        if (!this.selectedCard)
            return;
        const name = this.selectedCard.data('name');
        if (this.selectedNames.includes(name)) {
            $('#confirmModal').modal('hide');
            return;
        }
        this.selectedCard
                .addClass('card-selected')
                .find('.btn-select')
                .text('Votado')
                .prop('disabled', true);
        this.selectedNames.push(name);
        sessionStorage.setItem('votedNames', JSON.stringify(this.selectedNames));
        $('#confirmModal').modal('hide');

        if (this.selectedNames.length === 3) {
            this.generarConstanciaPDF();
            this.disableAllVoting();
            alert('Has votado 3 veces. La votación se ha cerrado.');
        }
    }

    generarConstanciaPDF() {
        const PDFClass = window.jspdf?.jsPDF || jsPDF;
        const doc = new PDFClass({unit: 'pt', format: 'letter'});
        const fechaStr = new Date().toLocaleString('es-PE', {dateStyle: 'long', timeStyle: 'short'});
        const m = 40;
        let y = 60;
        const pw = doc.internal.pageSize.getWidth();
        const ph = doc.internal.pageSize.getHeight();

        // Encabezado
        doc.setFont('helvetica', 'bold').setFontSize(13)
                .text('"UNIVERSIDAD NACIONAL JOSÉ FAUSTINO SÁNCHEZ CARRIÓN - HUACHO"', pw - m, y, {align: 'right'});

        // Logo izquierdo
        if (this.logoBase64) {
            doc.addImage(this.logoBase64, 'PNG', m, 40, 50, 50);
        }
        // Foto del votante (derecha)
        if (this.voter.photo) {
            doc.addImage(this.voter.photo, 'PNG', pw - m - 50, 40, 50, 50);
        }

        // Título
        y += 30;
        doc.setFont('helvetica', 'bold').setFontSize(16)
                .text('CONSTANCIA DE VOTACIÓN', pw / 2, y, {align: 'center'});

        // Cuerpo
        y += 30;
        doc.setFont('helvetica', 'bold').setFontSize(14).text('HACE CONSTAR:', m, y);
        y += 30;
        doc.setFont('Times', 'normal').setFontSize(12);
        // Mostrar Apellido primero y luego Nombre en PDF
        doc.text(`Nombre y Apellido: ${this.voter.apellido || ''} ${this.voter.nombre}`, m, y);
        y += 16;
        doc.text(`Código Universitario: ${this.voter.coduni}`, m, y);
        y += 16;
        doc.text(`Escuela Acad. Profesional: ${this.voter.escuela}`, m, y);

        y += 30;
        const cuerpo = `Muchas gracias por su sufragio... Ha participado exitosamente en la Votación Electrónica correspondiente al año académico 2025. Usted sufragó por estos candidatos:`;
        doc.text(cuerpo, m, y, {maxWidth: pw - 2 * m});

        y += 60;
        this.selectedNames.forEach((n, i) => {
            doc.text(`${i + 1}. ${n}`, m + 20, y);
            y += 18;
        });

        y += 10;
        doc.text(`Fecha y hora de emisión: ${fechaStr}`, m, y);

        // Pie de página
        const fy = ph - 60;
        doc.setFontSize(10);
        doc.text('C.C. Archivo', m, fy);
        doc.text('Rumbo a la acreditación total.', pw - m, fy, {align: 'right'});
        doc.text('Av. Mercedes Indacochea N° 609 – Ciudad Universitaria – Huacho', pw / 2, fy + 15, {align: 'center'});

        doc.save('constancia_voto.pdf');
    }
}

// Inicialización jsPDF + Votación
$(function () {
    const ready = setInterval(() => {
        if (window.jspdf?.jsPDF) {
            clearInterval(ready);
            new Votacion();
        }
    }, 100);
});
