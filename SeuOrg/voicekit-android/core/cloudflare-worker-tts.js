// cloudflare-worker-tts.js — Deploy no seu Worker existente
// Adicione esta rota ao lira.lionlaion369.workers.dev

export default {
  async fetch(request) {
    const url = new URL(request.url);
    
    if (url.pathname === '/tts') {
      const body = await request.json();
      const { text, voice = 'pt-BR-FranciscaNeural', rate = '+0%', pitch = '+0Hz' } = body;
      
      // Constrói SSML
      const ssml = `
        <speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='pt-BR'>
          <voice name='${voice}'>
            <prosody rate='${rate}' pitch='${pitch}'>
              ${text}
            </prosody>
          </voice>
        </speak>`;
      
      // Chama Azure Cognitive (free tier: 500k chars/mês)
      // OU redireciona para o motor edge-tts via servidor Node.js
      
      return new Response(JSON.stringify({ ssml, voice }), {
        headers: { 'Content-Type': 'application/json' }
      });
    }
  }
}
