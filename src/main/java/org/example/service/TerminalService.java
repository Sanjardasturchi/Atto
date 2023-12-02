package org.example.service;

import org.example.colors.StringColors;
import org.example.dto.ResponsDTO;
import org.example.dto.TerminalDTO;
import org.example.enums.Status;
import org.example.repository.TerminalRepository;

import java.util.List;

public class TerminalService {
    TerminalRepository terminalRepository = new TerminalRepository();

    public void creatTerminal(TerminalDTO terminal) {
        TerminalDTO terminal1 = terminalRepository.getTerminalByCode(terminal.getCode());
        if (terminal1.getAddress() !=null) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTerminal is already exists"+StringColors.ANSI_RESET);
            return;
        }

        ResponsDTO responsDTO = terminalRepository.creatTerminal(terminal);
        if (responsDTO.success()) {
            System.out.println(StringColors.BLUE + responsDTO.message() + StringColors.ANSI_RESET);
        } else {
            System.out.println(StringColors.RED + responsDTO.message() + StringColors.ANSI_RESET);
        }
    }

    public void showTerminalList() {
        List<TerminalDTO> terminalList = terminalRepository.getTerminalList();

        if (terminalList != null) {
            for (TerminalDTO terminalDTO : terminalList) {
                if (terminalDTO.getStatus().equals(Status.ACTIVE)) {
                    System.out.println(StringColors.YELLOW + terminalDTO);
                } else if (terminalDTO.getStatus().equals(Status.NO_ACTIVE)) {
                    System.out.println(StringColors.WHITE + terminalDTO);
                } else {
                    System.out.println(StringColors.RED + terminalDTO);
                }
            }
        } else {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAny terminals"+StringColors.ANSI_RESET);
        }
    }


    public void updateTerminal(TerminalDTO terminal, String address) {
        ResponsDTO responsDTO = terminalRepository.uptadeTerminal(terminal, address);
        if (responsDTO.success()) {
            System.out.println(StringColors.YELLOW + responsDTO.message());
        } else {
            System.out.println(StringColors.RED + responsDTO.message());
        }
    }

    public void changeTerminalStatus(String terminalCode, String status) {
        ResponsDTO responsDTO = terminalRepository.changeStatus(terminalCode, status);
        if (responsDTO.success()) {
            System.out.println(StringColors.YELLOW + responsDTO.message() + StringColors.ANSI_RESET);
        } else {
            System.out.println(StringColors.RED + responsDTO.message() + StringColors.ANSI_RESET);
        }
    }

    public void delete(String terminalCode, String status) {
        ResponsDTO responsDTO = terminalRepository.changeStatus(terminalCode, status);
        if (responsDTO.success()) {
            System.out.println(StringColors.YELLOW + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard deleted " + StringColors.ANSI_RESET);
        } else {
            System.out.println(StringColors.RED + responsDTO.message() + StringColors.ANSI_RESET);
        }
    }


}
