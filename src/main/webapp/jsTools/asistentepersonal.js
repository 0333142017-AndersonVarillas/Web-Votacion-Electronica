/**
 * asistentepersonal.js
 * Versión separada — comportamiento de la UI, reconocimiento y TTS.
 * Modo por defecto: AUDIO_ONLY = true (la IA habla; si deseas mostrar texto, pon AUDIO_ONLY = false)
 */

const AUDIO_ONLY = true;
const SHOW_TRANSCRIPT = false;

// Datos (compacto)
const detailedCandidates = [
    {nombre: "César Marcelino Mazuelos Cardoza", facultad: "Medicina", descripcion: "Estudiante de Medicina, promotor de proyectos de salud comunitaria."},
    {nombre: "Flor de María Lioo Jordán", facultad: "Industrial", descripcion: "Estudiante de Ingeniería Industrial, enfocada en optimización de procesos."},
    {nombre: "José Antonio Legua Cárdenas", facultad: "Civil", descripcion: "Estudiante de Ingeniería Civil, proponente de infraestructura sostenible."},
    {nombre: "Alejandro Manuel Salazar Santibañez", facultad: "Ingeniería", descripcion: "Ingeniero Industrial, coordinador académico y promotor de investigación aplicada."},
    {nombre: "Juan Carlos De Los Santos García", facultad: "Ingeniería", descripcion: "Magister en Ingeniería Industrial y líder en programas de innovación docente."},
    {nombre: "Juan Carlos Meyhuay Fidel", facultad: "Ingeniería", descripcion: "Magister en Informática, con 15 años de experiencia docente."},
    {nombre: "José Soto La Rosa", facultad: "Ingeniería", descripcion: "Magister en Sistemas, con 10 años de experiencia en investigación aplicada."},
    {nombre: "Jorge Antonio Sánchez Guzmán", facultad: "Sistemas", descripcion: "Licenciado en Sistemas, defensor de la calidad educativa y jefe de prácticas."},
    {nombre: "César Armando Díaz Valladares", facultad: "Informática", descripcion: "Licenciado en Informática, líder en bienestar estudiantil y proyectos académicos."},
    {nombre: "Delvis Beder Morales Escobar", facultad: "Industrial", descripcion: "Magister en Industrial, con amplia trayectoria en gestión institucional."}
];

const faqs = {
    "horario": "El horario de votación es de 8:00 a.m. a 6:00 p.m.",
    "procedimiento": "1. Presenta tu DNI.\n2. Identifícate con tu código universitario.\n3. Recibe tu boleta.\n4. Marca tu elección.\n5. Deposita el voto y recibe tu constancia.",
    "ubicación": "Ubica tu local de votación en la sección 'Transmisión del Sufragio' filtrando por distrito.",
    "constancia": "Tras votar, descarga tu constancia digital en la plataforma o solicítala impresa al jurado."
};

const extraResponses = {
    "cómo empiezo a votar": "Primero debes completar tu perfil con tus datos. Luego entra a la sección 'Votar', elige a tu candidato y confirma tu voto.",
    "dónde ingreso mis datos para votar": "En la sección 'Perfil de Votante', puedes ingresar tu nombre, código universitario y escuela profesional.",
    "necesito registrarme antes de votar": "Sí, es obligatorio completar tu perfil antes de emitir tu voto.",
    "qué pasa si no completo mi perfil": "No podrás acceder a la pantalla de votación. El sistema te redirigirá para que completes tu información.",
    "puedo votar desde mi celular": "Sí, el sistema es accesible desde computadoras, tablets y teléfonos móviles con conexión a internet.",
    "puedo votar más de una vez": "No. Solo puedes votar una vez por proceso electoral. El sistema bloquea votos duplicados.",
    "hasta qué hora se puede votar": "El cierre de votaciones está programado para las 6:00 p.m. del día asignado por el Comité Electoral.",
    "puedo cambiar mi voto si ya confirmé": "No. Una vez confirmado el voto, no se puede modificar por seguridad y transparencia.",
    "qué hago si me equivoqué al elegir": "Si aún no confirmaste, puedes cambiar tu selección. Pero si ya confirmaste, el voto queda registrado.",
    "se puede votar fuera del campus": "Sí. Solo necesitas una conexión a internet. Puedes votar desde cualquier lugar.",
    "cómo sé si mi voto fue registrado": "Al confirmar tu voto, verás un mensaje de éxito y podrás descargar una constancia como comprobante.",
    "cómo descargo mi constancia de votación": "Luego de votar, aparecerá un botón para descargar tu constancia en formato PDF.",
    "quiénes son los candidatos": () => {
        const list = detailedCandidates.map(c => `• ${c.nombre} (${c.facultad})`).join("\n");
        return `📋 Los candidatos son:\n${list}`;
    },
    "/candidatos": () => extraResponses["quiénes son los candidatos"](),
    "cuántos candidatos hay": () => `En esta elección participan ${detailedCandidates.length} candidatos registrados oficialmente.`,
    "de qué escuelas son los candidatos": () => {
        const schools = [...new Set(detailedCandidates.map(c => c.facultad))];
        return `Los candidatos provienen de: ${schools.join(', ')}.`;
    }
};

// Historial en localStorage
function saveMessage(role, text) {
    const history = JSON.parse(localStorage.getItem('chatHistory')) || [];
    history.push({role, text, timestamp: new Date().toISOString()});
    localStorage.setItem('chatHistory', JSON.stringify(history));
}
function getHistory() {
    return JSON.parse(localStorage.getItem('chatHistory')) || [];
}

// Helpers
function escapeHtml(unsafe) {
    if (!unsafe)
        return '';
    return unsafe.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
}

// Voz
let availableVoices = [];
function loadVoices() {
    availableVoices = window.speechSynthesis.getVoices() || [];
}
window.speechSynthesis.onvoiceschanged = loadVoices;

function selectSpanishVoice() {
    if (!availableVoices.length)
        loadVoices();
    const femalePatterns = [/female|mujer|woman/i, /maria|maría|sofia|sofía|ana|ines|carmen|isabel|laura|helena/i, /google español/i, /microsoft (.+) female/i, /es-/];
    let voice = availableVoices.find(v => v.lang && v.lang.toLowerCase().startsWith('es') && femalePatterns.some(p => p.test(v.name)));
    if (voice)
        return voice;
    voice = availableVoices.find(v => v.lang && v.lang.toLowerCase().startsWith('es'));
    if (voice)
        return voice;
    voice = availableVoices.find(v => femalePatterns.some(p => p.test(v.name)));
    if (voice)
        return voice;
    return availableVoices[0] || null;
}

function speak(text, opts = {}) {
    if (!('speechSynthesis' in window)) {
        $('#voice-status').text('🔇 Texto a voz no soportado en este navegador');
        return;
    }
    const utter = new SpeechSynthesisUtterance(text);
    utter.lang = opts.lang || 'es-PE';
    utter.rate = typeof opts.rate === 'number' ? opts.rate : 1;
    utter.pitch = typeof opts.pitch === 'number' ? opts.pitch : 1.05;
    utter.volume = typeof opts.volume === 'number' ? opts.volume : 1;
    const voice = selectSpanishVoice();
    if (voice)
        utter.voice = voice;

    utter.onstart = () => {
        $('#voice-status').text('🔊 Asistente hablando...');
        $('#big-mic').addClass('listening');
    };
    utter.onend = () => {
        $('#voice-status').text('');
        $('#big-mic').removeClass('listening');
    };
    window.speechSynthesis.cancel();
    window.speechSynthesis.speak(utter);
}

// Reconocimiento
let recognition = null;
function setupRecognition() {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SpeechRecognition) {
        $('#voice-status').text('Reconocimiento de voz no soportado.');
        return null;
    }
    recognition = new SpeechRecognition();
    recognition.lang = 'es-PE';
    recognition.interimResults = false;
    recognition.maxAlternatives = 1;

    recognition.onresult = event => {
        const text = event.results[0][0].transcript;
        $('#user-input').val(text);
        $('#voice-status').text('🎤 Capturado: "' + text + '"');
        $('#transcript-text').append('<div><strong>Tú:</strong> ' + escapeHtml(text) + '</div>');
        $('#transcript').scrollTop($('#transcript')[0].scrollHeight);
        setTimeout(() => $('#chat-form').submit(), 300);
    };

    recognition.onerror = e => {
        console.warn('Speech recognition error', e);
        $('#voice-status').text('Error en reconocimiento de voz');
        $('#big-mic').removeClass('listening');
    };

    recognition.onend = () => {
        $('#big-mic').removeClass('listening');
        setTimeout(() => {
            if ($('#voice-status').text().startsWith('🎤') === false)
                $('#voice-status').text('');
        }, 800);
    };
    return recognition;
}

// Procesamiento sencillo de comandos/respuestas
function procesarComando(msg) {
    const parts = msg.split(' ');
    const cmd = parts[0].toLowerCase();
    switch (cmd) {
        case '/help':
            return ['Comandos disponibles:', '/candidatos - Muestra lista completa', '/candidatos <nombre> - Detalle de un candidato', '/faq - Temas frecuentes', '/historial - Historial de chat', '/clear - Limpiar chat'].join('\n');
        case '/candidatos':
            if (parts.length === 1)
                return extraResponses['quiénes son los candidatos']();
            const name = parts.slice(1).join(' ').toLowerCase();
            const found = detailedCandidates.find(c => c.nombre.toLowerCase() === name);
            return found ? `🎓 ${found.nombre}\nFacultad: ${found.facultad}\nDescripción: ${found.descripcion}` : '❌ Candidato no encontrado.';
        case '/faq':
            return 'Temas FAQ disponibles: horario, procedimiento, ubicación, constancia.';
        case '/historial':
            const history = getHistory();
            return history.length ? history.map(m => `${m.role === 'user' ? 'Tú' : 'IA'} (${new Date(m.timestamp).toLocaleTimeString()}): ${m.text}`).join('\n') : '📂 Sin historial.';
        case '/clear':
            localStorage.removeItem('chatHistory');
            $('#chat-box').empty();
            return '🧹 Chat limpiado.';
        default:
            return null;
    }
}

function responderComoExperto(msg) {
    const texto = (msg || '').toLowerCase().trim();
    if (!texto)
        return '¿En qué puedo ayudarte?';
    if (texto.startsWith('/'))
        return procesarComando(texto);
    for (const key in extraResponses) {
        if (!key)
            continue;
        try {
            if (texto === key || texto.includes(key)) {
                const value = extraResponses[key];
                return (typeof value === 'function') ? value() : value;
            }
        } catch (e) {
        }
    }
    const candidato = detailedCandidates.find(c => texto.includes(c.nombre.toLowerCase()));
    if (candidato)
        return `🎓 ${candidato.nombre}\nFacultad: ${candidato.facultad}\nDescripción: ${candidato.descripcion}`;
    if (texto.includes('candidato') || texto.includes('candidatos') || texto.includes('quiénes son'))
        return extraResponses['quiénes son los candidatos']();
    if (texto.includes('procedimiento') || texto.includes('cómo votar') || texto.includes('cómo empiezo a votar') || texto.includes('pasos'))
        return extraResponses['cómo empiezo a votar'];
    for (const k in faqs)
        if (texto.includes(k))
            return faqs[k];
    if (/^hola\b|buenas|saludos/i.test(texto))
        return '¡Hola! Soy tu asistente IA. Puedes escribir o presionar el micrófono y preguntar en voz alta. Puedo responder por voz.';
    return '❓ No entendí tu consulta. Intenta con `/help` o pregunta por candidatos o el procedimiento de votación.';
}

// UI helpers
function appendUserMessage(text) {
    const safe = escapeHtml(text);
    $('#chat-box').append(`<div class="message user"><div class="text">${safe}</div></div>`);
    saveMessage('user', text);
    $('#chat-box').scrollTop($('#chat-box')[0].scrollHeight);
}
function appendAssistantMessage(text) {
    const finalText = (typeof text === 'function') ? text() : text;
    if (!AUDIO_ONLY) {
        const safe = escapeHtml(finalText).replace(/\n/g, '<br>');
        $('#chat-box').append(`<div class="message assistant"><div class="text">${safe}</div></div>`);
        saveMessage('assistant', finalText);
        $('#chat-box').scrollTop($('#chat-box')[0].scrollHeight);
    } else {
        saveMessage('assistant', finalText);
    }
}

// Init
$(document).ready(() => {
    loadVoices();
    setupRecognition();

    // Cargar historial pero solo usuarios visibles (IA solo audio)
    getHistory().forEach(m => {
        if (m.role === 'user') {
            $('#chat-box').append(`<div class="message user"><div class="text">${escapeHtml(m.text).replace(/\n/g, '<br>')}</div></div>`);
        }
    });
    $('#chat-box').scrollTop($('#chat-box')[0].scrollHeight);

    $('#chat-form').on('submit', e => {
        e.preventDefault();
        const userText = ($('#user-input').val() || '').trim();
        if (!userText)
            return;
        appendUserMessage(userText);
        $('#user-input').val('');
        setTimeout(() => {
            const response = responderComoExperto(userText);
            const responseText = response || 'Lo siento, no tengo una respuesta para eso.';
            speak(typeof responseText === 'function' ? responseText() : responseText, {lang: 'es-PE', rate: 1, pitch: 1.02});
            if (SHOW_TRANSCRIPT) {
                $('#transcript-text').append('<div><strong>IA:</strong> ' + escapeHtml(typeof responseText === 'function' ? responseText() : responseText) + '</div>');
                $('#transcript').scrollTop($('#transcript')[0].scrollHeight);
            }
            appendAssistantMessage(responseText);
        }, 250);
    });

    $('#big-mic').on('click', () => {
        if ($('#big-mic').hasClass('listening')) {
            if (recognition && typeof recognition.stop === 'function') {
                try {
                    recognition.stop();
                } catch (e) {
                }
            }
            $('#big-mic').removeClass('listening');
            $('#voice-status').text('');
            return;
        }
        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        if (!SpeechRecognition) {
            $('#voice-status').text('Reconocimiento de voz no soportado en este navegador.');
            return;
        }
        if (!recognition)
            setupRecognition();
        try {
            recognition.start();
            $('#big-mic').addClass('listening');
            $('#voice-status').text('🎙️ Escuchando... habla ahora');
        } catch (e) {
            console.warn('No se pudo iniciar reconocimiento', e);
            $('#voice-status').text('Error al iniciar reconocimiento.');
        }
    });

    $('#btn-stop-speech').on('click', () => {
        try {
            window.speechSynthesis.cancel();
        } catch (e) {
        }
        $('#voice-status').text('');
        $('#big-mic').removeClass('listening');
    });

    $('#btn-clear-history').on('click', () => {
        localStorage.removeItem('chatHistory');
        $('#chat-box').empty();
        $('#transcript-text').empty();
        $('#voice-status').text('Historial limpiado');
        setTimeout(() => $('#voice-status').text(''), 1200);
    });

    // refresco de voces
    setTimeout(loadVoices, 1000);
    setTimeout(loadVoices, 2000);
});
