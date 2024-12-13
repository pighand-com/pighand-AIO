const themes = {
    light: {
        primary: '#409eff',
        neutral: '#17191a',

        '.dy-btn-primary, .dy-btn-error': {
            color: '#fff'
        }
    }
};

const themeFormat = Object.keys(themes).map((theme) => {
    const themeConfig = {};
    Object.keys(themes[theme]).forEach((key) => {
        const value = themes[theme][key];
        themeConfig[key] = value;

        if (typeof value !== 'object') {
            themeConfig[`--${key}`] = value;
        }
    });

    return {
        [theme]: themeConfig
    };
});

export default {
    content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
    plugins: [require('daisyui')],
    daisyui: {
        prefix: 'dy-',
        themes: themeFormat
    }
};
