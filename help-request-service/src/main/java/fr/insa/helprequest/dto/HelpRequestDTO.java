package fr.insa.helprequest.dto;

import fr.insa.helprequest.model.HelpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequestDTO {
    private Long id;
    
    @NotNull(message = "L'ID de l'étudiant est obligatoire")
    private Long studentId;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    
    @NotBlank(message = "La description est obligatoire")
    private String description;
    
    private List<String> motsCles;
    private LocalDateTime dateCreation;
    private HelpRequest.StatutDemande statut;
    
    @NotNull(message = "Le type de demande est obligatoire")
    private HelpRequest.TypeDemande type;
}
