def completa_string(acesso_atual):
    if len(acesso_atual) != 8:
        zerosParaAdicionar = 8 - len(acesso_atual)
        zeros = "0" * zerosParaAdicionar
        acesso_atual = zeros + acesso_atual
    return acesso_atual[3:]

def compara_log(tamanho_referenceString, linhas_arquivo):
    print("Número de entradas na reference string:", tamanho_referenceString)
    print("Tamanho original do log:", linhas_arquivo)
    print("O tamanho da reference string é", (tamanho_referenceString / linhas_arquivo) * 100, "% do log original.")

def fileNextOcc(entracys, page, currentLine):
    for i, entry in enumerate(entracys[currentLine:], start=currentLine):
        if entry == page:
            return i + 1
    return 999999999

def main():
    try:
        filePath = "trace1"
        outputFile = "referencia1-OPT.txt"
        referenceString = []

        with open(filePath, 'r') as file:
            acesso_atual = completa_string(file.readline().strip()[:-3])
            referenceString.append(acesso_atual)

            linhas_arquivo = 1
            entracys = [acesso_atual]
            for line in file:
                proximo_acesso = completa_string(line.strip()[:-3])
                if acesso_atual != proximo_acesso:
                    referenceString.append(proximo_acesso)
                acesso_atual = proximo_acesso
                entracys.append(proximo_acesso)
                linhas_arquivo += 1

        with open(outputFile, 'w') as writer:
            for item in referenceString:
                writer.write("%s\n" % item)

        compara_log(len(referenceString), linhas_arquivo)
        addNextOccurrencesToReferenceFile(filePath, outputFile, entracys)

    except IOError as e:
        print("Erro de E/S:", e)

def addNextOccurrencesToReferenceFile(inputFile, outputFile, entracys):
    try:
        with open(inputFile, 'r') as reader, open(outputFile.replace('referencia1-OPT.txt', 'referencia1-OPT-occ.txt'), 'w') as writer:
            lineNumber = 1
            for line in reader:
                page = completa_string(line.strip()[:-3])
                nextOccurrence = fileNextOcc(entracys, page, lineNumber)
                writer.write(f"{page}, {nextOccurrence}\n")
                lineNumber += 1
                print(lineNumber)
    except IOError as e:
        print("Erro de E/S:", e)

if __name__ == "__main__":
    main()
