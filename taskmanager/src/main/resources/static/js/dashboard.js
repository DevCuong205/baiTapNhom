const ctx = document.getElementById('taskChart');

if (ctx) {
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Hoàn thành', 'Đang làm', 'Chưa làm'],
            datasets: [{
                data: [completed, doing, todo],
                backgroundColor: ['#22c55e', '#f59e0b', '#ef4444'],
                borderWidth: 0,
                hoverOffset: 8
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '68%',
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        usePointStyle: true,
                        pointStyle: 'circle',
                        padding: 20,
                        font: {
                            size: 14,
                            family: 'Be Vietnam Pro'
                        }
                    }
                },
                tooltip: {
                    backgroundColor: '#0f172a',
                    titleFont: {
                        family: 'Be Vietnam Pro'
                    },
                    bodyFont: {
                        family: 'Be Vietnam Pro'
                    }
                }
            }
        }
    });
}