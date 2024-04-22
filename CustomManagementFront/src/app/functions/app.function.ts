export function CheckEmail(email: string): Promise<boolean> {
    return new Promise<boolean>(resolve => {
        const emails: string[] = (email.replace(/\s+/g, '')).split(',');
        emails.forEach((email, index) => {
            if (!/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"0-9]+\.)+[^<>()[\]\.,;:\s@\"0-9]{2,})$/i.test(email)) {
                resolve(false);
            }

            if (index === emails.length - 1) {
                resolve(true);
            }
        });
    });
}

export function CharactersAllowed(paragraph: string, option: number): string {
    const pattern1 = /[A-Za-z0-9\@\.]/;             // Letras, numeros, arroba y puntos
    const pattern2 = /[A-Za-z\sñÑ]/;                // Letras y espacios
    const pattern3 = /[0-9]/;                       // Solo numeros
    const pattern4 = /[A-Za-zñÑ]/;                  // Solo letras

    let newParagraph = '';
    for (const letter of paragraph) {
        if (option === 1 && !pattern1.test(letter)) {
            newParagraph += letter.replace(letter, '');
        } else if (option === 1 && pattern1.test(letter)) {
            newParagraph += letter;
        } else if (option === 2 && !pattern2.test(letter)) {
            newParagraph += letter.replace(letter, '');
        } else if (option === 2 && pattern2.test(letter)) {
            newParagraph += letter;
        } else if (option === 3 && !pattern3.test(letter)) {
            newParagraph += letter.replace(letter, '');
        } else if (option === 3 && pattern3.test(letter)) {
            newParagraph += letter;
        } else if (option === 4 && !pattern4.test(letter)) {
            newParagraph += letter.replace(letter, '');
        } else if (option === 4 && pattern4.test(letter)) {
            newParagraph += letter;
        }
    }

    return newParagraph;
}