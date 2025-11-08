import React, { useState } from "react";

const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080/api";

function App() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState([]);

  async function sendMessage(e) {
    e?.preventDefault();
    if (!input.trim()) return;
    const userMsg = { role: "user", content: input };
    setMessages((m) => [...m, { sender: "You", text: input }]);
    const body = { message: input };

    setInput("");

    try {
      const res = await fetch(`${API_BASE}/chat`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      if (!res.ok) {
        const txt = await res.text();
        throw new Error(`Server error: ${txt}`);
      }

      const data = await res.json();
      // Try to extract a reply if the response follows common schema
      let reply = "";
      if (data?.choices && data.choices[0]?.message?.content) {
        reply = data.choices[0].message.content;
      } else if (data?.output || data?.result) {
        reply = data.output ?? data.result;
      } else {
        reply = JSON.stringify(data);
      }

      setMessages((m) => [...m, { sender: "Bot", text: reply }]);
    } catch (err) {
      setMessages((m) => [...m, { sender: "System", text: "Error: " + err.message }]);
    }
  }

  return (
    <div className="app">
      <h1>AI Chat</h1>
      <div className="chat-window">
        {messages.map((msg, i) => (
          <div key={i} className={`message ${msg.sender}`}>
            <strong>{msg.sender}:</strong> <span>{msg.text}</span>
          </div>
        ))}
      </div>

      <form onSubmit={sendMessage} className="input-form">
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Type a message..."
        />
        <button type="submit">Send</button>
      </form>
    </div>
  );
}

export default App;
