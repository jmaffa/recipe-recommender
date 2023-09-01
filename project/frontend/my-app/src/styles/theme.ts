import { createTheme } from "@mui/material";
import { typography } from "@mui/system";

// some work with declaring custom typography types, idk...
// declare module '@mui/material/styles' {
//     interface Theme {
//         typography: {
//             nav: string
//         }
//     }
//     interface ThemeOptions {
//         typography?: {
//             nav?: string
//         }
//     }
// }
const theme = createTheme({
    palette: {
        primary: {
          main: '#2e818a',
          contrastText: '#ffe1c3',
        },
        secondary: {
          main: '#820051',
        },
        background: {
          default: '#D6D2C4',
          paper: '#D9D9D9',
        },
        text: {
          primary: '#003B49',
          secondary: '#003B49',
          disabled: '#003B49',
        },
        info: {
          main: '#2196f3',
        },
    },
    typography: {
        fontFamily: 'Verdana, sans-serif',
        h1: {
            fontWeight: 300,
            fontSize: '500',
            color: '#003B49'
        },
        h2: {
          fontWeight: 300,
          fontSize: '250', // this isn't doing anything idrk why
          color: '#003B49'
        },
        body1: {
            fontWeight: 300,
            fontSize: '200',
            color: '#000000'
        },
    },
    spacing: 10
}
)

export default theme;
