// jsTools/reclamos.js
(function () {
    const form = document.getElementById("chatForm");
    const input = document.getElementById("mensaje");
    const chatBox = document.getElementById("chatBox");

    // Perfil desde sessionStorage
    const perfil = JSON.parse(sessionStorage.getItem('voterProfile'));
    if (!perfil || !perfil.coduni) {
        alert('Primero complete su perfil de votante.');
        window.location.href = 'perfilvotante.html';
        return;
    }

    const STORAGE_KEY = 'reclamosChat';

    // Cargar y dibujar mensajes
    function cargarMensajes() {
        chatBox.innerHTML = '';
        const msgs = JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];
        msgs.forEach(m => agregarMensajeElemento(m));
        scrollAlFinal();
    }

    // Convertir texto con @CODUNI en spans
    function formatearMenciones(texto) {
        return texto.replace(/@(\w+)/g, '<span class="mention">@$1</span>');
    }

    // Añadir burbuja al chat
    function agregarMensajeElemento( { nombre, coduni, mensaje }) {
        const wrapper = document.createElement("div");
        wrapper.classList.add("chat-message", coduni === perfil.coduni ? "user" : "admin");
        wrapper.innerHTML = `
            <div class="chat-text">${formatearMenciones(mensaje)}</div>
            <div class="chat-meta">${nombre} (${coduni})</div>
        `;
        chatBox.appendChild(wrapper);
    }

    // Guardar nuevo mensaje
    function enviarMensaje(texto) {
        const nuevo = {
            nombre: `${perfil.nombre} ${perfil.apellido || ''}`.trim(),
            coduni: perfil.coduni,
            mensaje: texto
        };
        const msgs = JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];
        msgs.push(nuevo);
        localStorage.setItem(STORAGE_KEY, JSON.stringify(msgs));
        agregarMensajeElemento(nuevo);
        scrollAlFinal();
    }

    function scrollAlFinal() {
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    // Evento envío
    form.addEventListener("submit", e => {
        e.preventDefault();
        const texto = input.value.trim();
        if (!texto)
            return;
        enviarMensaje(texto);
        input.value = '';
    });

    // Actualizaciones de otras pestañas
    window.addEventListener('storage', e => {
        if (e.key === STORAGE_KEY)
            cargarMensajes();
    });

    // Init
    cargarMensajes();
})();
