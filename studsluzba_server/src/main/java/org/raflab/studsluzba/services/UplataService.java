package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.PreostaliIznosResponse;
import org.raflab.studsluzba.controllers.response.UplataResponse;
import org.raflab.studsluzba.model.IzlazakNaIspit;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.Uplata;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.UplataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UplataService {
    /*
    private final UplataRepository uplataRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final SlusaPredmetRepository slusaPredmetRepository;
    private static final String KURS_API_URL =
            "https://kurs.resenje.org/api/v1/currencies/eur/rates/today";

    @Transactional
    public UplataResponse createUplata(UplataRequest request) {

        Double srednjiKurs = fetchSrednjiKurs();

        Uplata uplata = new Uplata();
        uplata.setDatumUplate(
                request.getDatumUplate() != null ? request.getDatumUplate() : LocalDate.now()
        );
        uplata.setIznos(request.getIznos());
        uplata.setSrednjiKurs(srednjiKurs);

        Uplata saved = uplataRepository.save(uplata);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<UplataResponse> getAll() {
        return uplataRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UplataResponse getById(Long id) {
        return uplataRepository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    @Transactional
    public void delete(Long id) {
        uplataRepository.deleteById(id);
    }


    private Double fetchSrednjiKurs() {
        try {
            Map<String, Object> result =
                    restTemplate.getForObject(KURS_API_URL, Map.class);

            return result != null && result.get("middle") != null
                    ? Double.valueOf(result.get("middle").toString())
                    : null;

        } catch (Exception e) {
            throw new RuntimeException("Greška pri dobavljanju srednjeg kursa!", e);
        }
    }

    private UplataResponse toResponse(Uplata u) {
        UplataResponse r = new UplataResponse();
        r.setId(u.getId());
        r.setDatumUplate(u.getDatumUplate());
        r.setIznos(u.getIznos());
        r.setSrednjiKurs(u.getSrednjiKurs());
        return r;
    }

    @Transactional(readOnly = true)
    public PreostaliIznosResponse getPreostaliIznos(Long studentIndeksId) {
        final double UKUPNA_SKOLARINA_EUR = 3000.0;

        // Dohvati sve uplate za studenta (pretpostavljamo da postoji veza StudentIndeks -> Uplata)
        List<Uplata> uplate = uplataRepository.findAll()
                .stream()
                .filter(u -> u.getStudentIndeks() != null && u.getStudentIndeks().getId().equals(studentIndeksId))
                .collect(Collectors.toList());

        double uplacenoDin = uplate.stream()
                .mapToDouble(u -> u.getIznos() != null ? u.getIznos() : 0)
                .sum();

        // Pretpostavljamo da je srednji kurs poslednje uplate ili možemo uzeti prosek
        double poslednjiKurs = uplate.isEmpty() ? fetchSrednjiKurs() : uplate.get(uplate.size() - 1).getSrednjiKurs();

        double uplacenoEur = uplacenoDin / poslednjiKurs;
        double preostaloEur = UKUPNA_SKOLARINA_EUR - uplacenoEur;
        double preostaloDin = preostaloEur * poslednjiKurs;

        PreostaliIznosResponse response = new PreostaliIznosResponse();
        response.setPreostaloEur(preostaloEur);
        response.setPreostaloDin(preostaloDin);
        return response;
    }
    @Transactional(readOnly = true)
    public Page<SlusaPredmet> getPolozeniIspiti(Long studentIndeksId, Pageable pageable) {
        List<SlusaPredmet> sviPredmeti = slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(studentIndeksId);

        List<SlusaPredmet> polozeni = sviPredmeti.stream()
                .filter(sp -> {
                    List<IzlazakNaIspit> izlazi = sp.getIzlazakNaIspite() != null
                            ? sp.getIzlazakNaIspite().stream()
                            .filter(i -> !i.isPonistio() && i.getOstvarenoNaIspitu() != null && i.getOstvarenoNaIspitu() >= 6)
                            .collect(Collectors.toList())
                            : List.of();
                    return !izlazi.isEmpty();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(polozeni, pageable, polozeni.size());
    }

    @Transactional(readOnly = true)
    public Page<SlusaPredmet> getNepolozeniIspiti(Long studentIndeksId, Pageable pageable) {
        List<SlusaPredmet> sviPredmeti = slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(studentIndeksId);

        List<SlusaPredmet> nepolozeni = sviPredmeti.stream()
                .filter(sp -> {
                    List<IzlazakNaIspit> izlazi = sp.getIzlazakNaIspite() != null
                            ? sp.getIzlazakNaIspite().stream()
                            .filter(i -> !i.isPonistio())
                            .collect(Collectors.toList())
                            : List.of();
                    // nepolozeni = nema nijednog sa ocenom >= 6
                    return izlazi.isEmpty() || izlazi.stream().allMatch(i -> i.getOstvarenoNaIspitu() == null || i.getOstvarenoNaIspitu() < 6);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(nepolozeni, pageable, nepolozeni.size());
    }

*/
}
