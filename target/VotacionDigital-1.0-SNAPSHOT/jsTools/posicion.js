// Función para simular fetch de datos en vivo
function fetchLiveData() {
    return new Promise(resolve => {
        setTimeout(() => {
            const data = {
                timestamp: new Date(),
                summary: {
                    positions: 3,
                    candidates: 9,
                    votes: Math.floor(Math.random() * 100),
                    voters: Math.floor(Math.random() * 80)
                },
                rector: {
                    labels: ["César Mazuelos", "Flor Lioo", "José Legua"],
                    votes: [Math.random() * 10, Math.random() * 10, Math.random() * 10]
                },
                decano: {
                    labels: ["A. Salazar", "J. Santos", "J. Meyhuay", "J. Soto"],
                    votes: [Math.random() * 10, Math.random() * 10, Math.random() * 10, Math.random() * 10]
                },
                director: {
                    labels: ["J. Sánchez", "C. Díaz", "D. Morales"],
                    votes: [Math.random() * 10, Math.random() * 10, Math.random() * 10]
                }
            };
            resolve(data);
        }, 500);
    });
}

// Inicializa gráficos
const charts = {};
function initChart(ctx, type, dataSet) {
    return new Chart(ctx, {
        type: 'bar',
        data: {
            labels: dataSet.labels,
            datasets: [{label: "# Votes", data: dataSet.votes}]
        },
        options: {
            indexAxis: 'y',
            scales: {x: {beginAtZero: true}},
            animation: {duration: 500}
        }
    });
}

// Render progress bars
function renderProgress(containerId, labels, votes) {
    const total = votes.reduce((a, b) => a + b, 0) || 1;
    const container = document.getElementById(containerId);
    container.innerHTML = '';
    labels.forEach((lbl, i) => {
        const pct = ((votes[i] / total) * 100).toFixed(1);
        const bar = document.createElement('div');
        bar.className = 'mb-2';
        bar.innerHTML = `
            <div class="d-flex justify-content-between">
                <small>${lbl}</small><small>${pct}%</small>
            </div>
            <div class="progress">
                <div class="progress-bar" role="progressbar" style="width:${pct}%"></div>
            </div>`;
        container.appendChild(bar);
    });
}

// Actualiza todo
async function updateLive() {
    const live = await fetchLiveData();

    // Summary
    $('#count-positions').text(live.summary.positions);
    $('#count-candidates').text(live.summary.candidates);
    $('#count-votes').text(live.summary.votes);
    $('#count-voters').text(live.summary.voters);
    $('#last-updated').text('Última actualización: ' + live.timestamp.toLocaleTimeString());

    // Charts & progress
    if (!charts.rector) {
        charts.rector = initChart($('#chartRector')[0].getContext('2d'), 'bar', live.rector);
        charts.decano = initChart($('#chartDecano')[0].getContext('2d'), 'bar', live.decano);
        charts.director = initChart($('#chartDirector')[0].getContext('2d'), 'bar', live.director);
    } else {
        charts.rector.data.labels = live.rector.labels;
        charts.rector.data.datasets[0].data = live.rector.votes;
        charts.rector.update();

        charts.decano.data.labels = live.decano.labels;
        charts.decano.data.datasets[0].data = live.decano.votes;
        charts.decano.update();

        charts.director.data.labels = live.director.labels;
        charts.director.data.datasets[0].data = live.director.votes;
        charts.director.update();
    }

    renderProgress('progress-rector', live.rector.labels, live.rector.votes);
    renderProgress('progress-dec', live.decano.labels, live.decano.votes);
    renderProgress('progress-dir', live.director.labels, live.director.votes);
}

// Arranca polling cada 5 segundos
$(document).ready(() => {
    updateLive();
    setInterval(updateLive, 5000);
});
